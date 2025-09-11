package com.reddit.coreService.service;

import com.reddit.coreService.client.UserManagementClient;
import com.reddit.coreService.controller.dto.response.*;
import com.reddit.coreService.exceptions.*;
import com.reddit.coreService.mapper.CommunityMapper;
import com.reddit.coreService.mapper.MemberRequestMapper;
import com.reddit.coreService.mapper.ModeratorMapper;
import com.reddit.coreService.model.*;
import com.reddit.coreService.repository.*;
import com.reddit.coreService.service.dto.request.CreateCommunityCommand;
import com.reddit.coreService.service.dto.request.DecideJoinCommand;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommunityService {
    private final CommunityMapper communityMapper;
    private final CommunityRepository communityRepository;
    private final UserManagementClient userClient;
    private final ModeratorRepository moderatorRepository;
    private final ModeratorPrivilegeRepository moderatorPrivilegeRepository;
    private final PrivilegeRepository privilegeRepository;
    private final CommunityMemberRepository communityMemberRepository;
    private final UserValidationService userValidationService;
    private final MemberRequestRepository memberRequestRepository;
    private final MemberRequestMapper memberRequestMapper;
    private final ModeratorMapper moderatorMapper;
    @Transactional
    public CommunityResponse createCommunity(CreateCommunityCommand command) {
        // Validate user
        UserResponse user = userValidationService.validateUserExistsAndLoggedIn(command.userId());

        // Check for duplicate community name
        communityRepository.findByName(command.name())
                .ifPresent(c -> {
                    throw new ConflictException("Community with name " + command.name() + " already exists!");
                });

        // Parse community type
        CommunityType communityType;
        try {
            communityType = CommunityType.valueOf(command.type().toString().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid community type: " + command.type());
        }

        // Create community
        Community community = new Community();
        community.setName(command.name());
        community.setDescription(command.description());
        community.setType(communityType);
        Community savedCommunity = communityRepository.save(community);

        // Create moderator for the creator
        Moderator moderator = new Moderator();
        moderator.setUserId(command.userId());
        moderator.setCommunity(savedCommunity);
        moderator.setAssignedBy(null);
        Moderator savedModerator = moderatorRepository.save(moderator);

        // Assign all privileges to the creator
        List<Privilege> allPrivileges = privilegeRepository.findAllDeletedFalse();
        for (Privilege privilege : allPrivileges) {
            ModeratorPrivilege mp = new ModeratorPrivilege();
            mp.setModerator(savedModerator);
            mp.setPrivilege(privilege);
            moderatorPrivilegeRepository.save(mp);
        }

        // Add creator as a member
        CommunityMember member = new CommunityMember();
        member.setUserId(command.userId());
        member.setCommunity(savedCommunity);
        communityMemberRepository.save(member);

        return communityMapper.fromCommunity(savedCommunity);
    }

    @Transactional
    public JoinCommunityResponse joinCommunity(long userId, long communityId) {
        // Validate user
      //  UserResponse user = userValidationService.validateUserExistsAndLoggedIn(userId);

        // Check community exists
        Community community = communityRepository.findByIdAndDeletedFalse(communityId)
                .orElseThrow(() -> new ResourceNotFoundException("Community not found or inactive"));

        // Check if already a member
        boolean alreadyMember = communityMemberRepository.existsByUserIdAndCommunityIdAndDeletedFalse(userId, communityId);
        if (alreadyMember) {
            throw new ConflictException("User is already a member of this community");
        }

        JoinCommunityResponse response = new JoinCommunityResponse();
        response.setCommunityId(communityId);
        response.setUserId(userId);

        if (community.getType() == CommunityType.PUBLIC) {
            CommunityMember newMember = new CommunityMember();
            newMember.setUserId(userId);
            newMember.setCommunity(community);
            communityMemberRepository.save(newMember);
            response.setMessage("Joined successfully");
        } else if (community.getType() == CommunityType.PRIVATE) {
            Optional<MemberRequest> existingRequestOpt = memberRequestRepository
                    .findByUserIdAndCommunityIdAndStatus(userId, communityId, RequestStatus.PENDING);

            if (existingRequestOpt.isPresent()) {
                throw new ConflictException("You already have a pending join request for this community");
            }

            MemberRequest request = new MemberRequest();
            request.setCommunity(community);
            request.setUserId(userId);
            request.setStatus(RequestStatus.PENDING);
            memberRequestRepository.save(request);

            response.setMessage("Join request sent. Waiting for moderator approval");
        } else {
            throw new ValidationException("Cannot join this community type: " + community.getType());
        }

        return response;
    }

    @Transactional
    public Page<MemberPendingRequestResponse> getPendingRequests(Long communityId, Long userId, Pageable pageable) {
        // Validate user

        // Check community exists
        Community community = communityRepository.findByIdAndDeletedFalse(communityId)
                .orElseThrow(() -> new ResourceNotFoundException("Community not found or inactive"));

        // Only private communities allow pending requests
        if (community.getType() == CommunityType.PUBLIC) {
            throw new ForbiddenException("Action allowed only in private communities");
        }

        // Validate moderator privileges
        Moderator moderator = validateModeratorHasPrivilege(userId, communityId, PrivilegeName.MANAGE_MEMBERS);

        Page<MemberRequest> page = memberRequestRepository.findPendingRequestsByCommunityId(communityId, pageable);



        //return page.map(memberRequestMapper::toMemberPendingRequestResponse);
        Page<MemberPendingRequestResponse> page1 = page.map(m -> {
            try {
                UserResponse user = userClient.getUserById(m.getUserId());
                Long commId = m.getCommunity() != null ? m.getCommunity().getId() : null;
                Long assignedById = m.getAssignedBy() != null ? m.getAssignedBy().getId() : null;

                return new MemberPendingRequestResponse(
                        user,
                        commId,
                        m.getStatus(),
                        m.getCreatedAt(),
                        assignedById
                );
            } catch (Exception e) {
                System.out.println("Error mapping MemberPendingRequest for userId={}"+m.getUserId()+ e);
                throw e;
            }
        });

        return page1;
    }


    @Transactional
    public DecideJoinResponse decideRequest(Long requestId, DecideJoinCommand decideJoinCommand) {
        UserResponse user = userValidationService.validateUserExistsAndLoggedIn(decideJoinCommand.userId());

        MemberRequest memberRequest = memberRequestRepository.findWithAssignedBy(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request with ID " + requestId + " not found or canceled"));

        if (memberRequest.getStatus() != RequestStatus.PENDING) {
            throw new ConflictException("This join request has already been decided.");
        }

        Moderator moderator = validateModeratorHasPrivilege(
                decideJoinCommand.userId(),
                memberRequest.getCommunity().getId(),
                PrivilegeName.MANAGE_MEMBERS
        );

        RequestStatus status;
        try {
            status = RequestStatus.valueOf(decideJoinCommand.status().toString());
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid request status: " + decideJoinCommand.status());
        }

        if (status != RequestStatus.ACCEPTED && status != RequestStatus.REJECTED) {
            throw new ValidationException("Only ACCEPTED or REJECTED are allowed for deciding a join request.");
        }

        memberRequest.setStatus(status);
        memberRequest.setAssignedBy(moderator);
        memberRequestRepository.save(memberRequest);

        return memberRequestMapper.toDecideJoinResponse(memberRequest);
    }


    public Moderator validateModeratorHasPrivilege(Long userId, Long communityId, PrivilegeName requiredPrivilege) {
        Moderator moderator = moderatorRepository.findByUserIdAndCommunityIdAndDeletedFalse(userId, communityId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Moderator for user " + userId + " in community " + communityId + " not found or inactive"
                ));

        List<PrivilegeName> privileges = moderatorPrivilegeRepository.findActivePrivilegesByModerator(moderator);

        if (!privileges.contains(requiredPrivilege)) {
            throw new ForbiddenException(
                    "Moderator does not have " + requiredPrivilege + " privilege in community " + communityId
            );
        }

        return moderator;
    }

    public Moderator validateModeratorHasPrivilege(
            long moderatorId,
            PrivilegeName requiredPrivilege
    ) {
        Moderator moderator = moderatorRepository.findByIdAndDeletedFalse(moderatorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Moderator with ID " + moderatorId + " not found or inactive "
                ));

        List<PrivilegeName> privileges = moderatorPrivilegeRepository.findActivePrivilegesByModerator(moderator);

        if (!privileges.contains(requiredPrivilege)) {
            throw new ForbiddenException(
                    "Moderator with ID " + moderatorId + " does not have " + requiredPrivilege + " privilege"
            );
        }

        return moderator;
    }

    public Page<CommunityModeratorsPrivilegesResponse> getModeratorsPrivileges(Long userId ,Long communityId, Pageable pageable) {
        UserResponse user = userValidationService.validateUserExistsAndLoggedIn(userId);

        Moderator moderator = validateModeratorHasPrivilege(
                userId,
                PrivilegeName.MANAGE_MEMBERS
        );
        Page<Moderator> page = moderatorRepository.findByCommunityId(communityId, pageable);

        if (page.isEmpty()) return Page.empty(pageable);

        List<Long> moderatorIds = page.stream()
                .map(Moderator::getId)
                .toList();

        List<ModeratorPrivilege> privileges = moderatorRepository.findPrivilegesByModeratorIds(moderatorIds);

        Map<Long, List<ModeratorPrivilege>> privilegeMap = privileges.stream()
                .collect(Collectors.groupingBy(mp -> mp.getModerator().getId()));

        return page.map(mod ->
                moderatorMapper.toResponse
                        (mod, privilegeMap.getOrDefault(moderator.getId(), List.of()))
        );
    }



    public List<CommunityPrivilegesResponse> getUserModeratorPrivileges(Long userId) {
        List<Moderator> moderators = moderatorRepository.findByUserIdAndDeletedFalse(userId);
        if (moderators.isEmpty()) {
            return Collections.emptyList();
        }

        List<ModeratorPrivilege> privileges = moderatorPrivilegeRepository.findByModeratorIn(moderators);

        // Group privileges by communityId
        Map<Long, List<String>> communityPrivileges = privileges.stream()
                .collect(Collectors.groupingBy(
                        mp -> mp.getModerator().getCommunity().getId(),
                        Collectors.mapping(mp -> mp.getPrivilege().getName().name(), Collectors.toList())
                ));

        // Convert map to DTO list
        return communityPrivileges.entrySet().stream()
                .map(e -> new CommunityPrivilegesResponse(e.getKey(), e.getValue()))
                .toList();
    }

}
