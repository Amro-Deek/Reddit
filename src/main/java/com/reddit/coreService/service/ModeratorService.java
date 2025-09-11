package com.reddit.coreService.service;

import com.reddit.coreService.client.UserManagementClient;
import com.reddit.coreService.controller.dto.response.AssignModeratorResponse;
import com.reddit.coreService.controller.dto.response.RegisterUserResponse;
import com.reddit.coreService.controller.dto.response.UserResponse;
import com.reddit.coreService.exceptions.*;
import com.reddit.coreService.mapper.CommunityMapper;
import com.reddit.coreService.mapper.MemberRequestMapper;
import com.reddit.coreService.mapper.ModeratorMapper;
import com.reddit.coreService.model.*;
import com.reddit.coreService.repository.*;
import com.reddit.coreService.service.dto.request.AssignModeratorCommand;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModeratorService {
    private final ModeratorMapper moderatorMapper;
    private final ModeratorRepository moderatorRepository;
    private final CommunityMapper communityMapper;
    private final CommunityRepository communityRepository;
    private final UserManagementClient userClient;
    private final ModeratorPrivilegeRepository moderatorPrivilegeRepository;
    private final PrivilegeRepository privilegeRepository;
    private final CommunityMemberRepository communityMemberRepository;
    private final UserValidationService userValidationService;
    private final MemberRequestRepository memberRequestRepository;
    private final MemberRequestMapper memberRequestMapper;
    private final MemberRepository memberRepository;

    @Transactional
    public AssignModeratorResponse assignModerator(
            long moderatorId,
            @Valid AssignModeratorCommand assignModeratorRequest
    ) {
        long userId = assignModeratorRequest.userId();

        // ✅ Validate user existence
        UserResponse user = userValidationService.validateUserExistsAndLoggedIn(userId);
        UserResponse moderatorUser = userValidationService.validateUserExistsAndLoggedIn(moderatorId);



        // ✅ Ensure assigning moderator has MANAGE_MODERATORS privilege
        Moderator assigningModerator = validateModeratorHasPrivilege(
                moderatorId, PrivilegeName.MANAGE_MODERATORS
        );
        // ✅ Ensure community exists
        long communityId  = assigningModerator.getCommunity().getId();
        Community community = communityRepository.
                findByIdAndDeletedFalse(assigningModerator.getCommunity().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Community with ID " + communityId + " not found or inactive"
                ));
        memberRepository.findByUserIdAndCommunityIdAndDeletedFalse(userId, communityId)
                .orElseThrow(() -> new InvalidInputException(
                        "User with ID " + userId + " must be a member of the community before becoming a moderator"
                ));

        // ✅ Create new moderator
        Moderator newModerator = new Moderator();
        newModerator.setUserId(userId);
        newModerator.setCommunity(community);
        newModerator.setAssignedBy(assigningModerator);
        moderatorRepository.save(newModerator);

        // ✅ Validate privilege names and fetch from repo
        List<PrivilegeName> privilegeNames = assignModeratorRequest.privilegeNames();
        List<Privilege> privileges = privilegeNames.stream()
                .map(name -> {
                    Privilege privilege = privilegeRepository.findByName(name);
                    if (privilege == null) {
                        throw new InvalidInputException("Invalid privilege name: " + name);
                    }
                    return privilege;
                })
                .toList();

        // ✅ Assign privileges
        for (Privilege privilege : privileges) {
            ModeratorPrivilege mp = new ModeratorPrivilege();
            mp.setModerator(newModerator);
            mp.setPrivilege(privilege);
            moderatorPrivilegeRepository.save(mp);
        }

        return new AssignModeratorResponse(newModerator.getId(), communityId, privilegeNames);
    }


    public Moderator validateModeratorHasPrivilege(
            Long userId,
            Long communityId,
            PrivilegeName requiredPrivilege
    ) {
        Moderator moderator = moderatorRepository
                .findByUserIdAndCommunityIdAndDeletedFalse(userId, communityId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Moderator with user ID " + userId + " not found or inactive in community " + communityId
                ));

        List<PrivilegeName> privileges = moderatorPrivilegeRepository.findActivePrivilegesByModerator(moderator);

        if (!privileges.contains(requiredPrivilege)) {
            throw new ForbiddenException(
                    "Moderator with user ID " + userId + " does not have " + requiredPrivilege + " privilege"
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


}
