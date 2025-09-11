package com.reddit.coreService.controller;


import com.reddit.coreService.controller.dto.request.CreateCommunityRequest;
import com.reddit.coreService.controller.dto.request.DecideJoinRequest;
import com.reddit.coreService.controller.dto.request.JoinCommunityRequest;
import com.reddit.coreService.controller.dto.response.*;
import com.reddit.coreService.mapper.CommunityMapper;
import com.reddit.coreService.mapper.MemberRequestMapper;
import com.reddit.coreService.repository.CommunityRepository;
import com.reddit.coreService.security.JwtAuthentication;
import com.reddit.coreService.service.CommunityService;
import com.reddit.coreService.service.MemberService;
import com.reddit.coreService.service.dto.request.CreateCommunityCommand;
import com.reddit.coreService.service.dto.request.DecideJoinCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityRepository communityRepository;
    private final CommunityMapper communityMapper;
    private final CommunityService communityService;
    private final MemberRequestMapper memberRequestMapper;
    private final MemberService memberService;


    @PostMapping()
    public ResponseEntity<CommunityResponse> createCommunity(
            @Valid @RequestBody CreateCommunityRequest createCommunityRequest) {

        CreateCommunityCommand createCommunityCommand = communityMapper
                .fromCreateCommunityRequest(createCommunityRequest);

        CommunityResponse communityResponse = communityService
                .createCommunity(createCommunityCommand);

        return ResponseEntity.ok(communityResponse);
    }

    @PostMapping("/{communityId}/join")
    public ResponseEntity<JoinCommunityResponse> joinCommunity(
            @PathVariable long communityId,
            @AuthenticationPrincipal JwtAuthentication principal
            ) {

        JoinCommunityResponse joinCommunityResponse = communityService.joinCommunity(principal.getUserId(), communityId);
        return ResponseEntity.ok(joinCommunityResponse);
    }

    //is it good to write an end point that fetches all requests according to given Status?
    @GetMapping("/{communityId}/join-requests")
    public ResponseEntity<PaginatedResponse<MemberPendingRequestResponse>> getPendingRequests(
            @PathVariable Long communityId,
            @AuthenticationPrincipal JwtAuthentication principal,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable){
        Page<MemberPendingRequestResponse> page =
                communityService.getPendingRequests(communityId,principal.getUserId(),pageable);
        PaginatedResponse<MemberPendingRequestResponse> body =
                PaginatedResponse.of(page);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/join-requests/{requestId}/decision")
    public ResponseEntity<DecideJoinResponse> decideJoinRequest(
            @PathVariable Long requestId,
            @RequestBody @Valid DecideJoinRequest request) {

        DecideJoinCommand decideJoinCommand = memberRequestMapper.toDecideJoinCommand(request);
        DecideJoinResponse decideJoinResponse = communityService.decideRequest(requestId,decideJoinCommand);
        return ResponseEntity.ok(decideJoinResponse);
    }

    @GetMapping("/{communityId}/moderators-privileges")
    public ResponseEntity<PaginatedResponse<CommunityModeratorsPrivilegesResponse>>
    getModeratorsPrivileges(
            @PathVariable Long communityId,
            @RequestParam Long userId,
            @PageableDefault(page = 0, size = 10, sort = "id",
                    direction = Sort.Direction.DESC) Pageable pageable){
        Page<CommunityModeratorsPrivilegesResponse> page = communityService
                .getModeratorsPrivileges(userId,communityId, pageable);
        PaginatedResponse<CommunityModeratorsPrivilegesResponse> body =
                PaginatedResponse.of(page);
        return  ResponseEntity.ok(body);
    }


    @GetMapping("/{userId}/communities-privileges")
    public List<CommunityPrivilegesResponse> getModeratorPrivileges(@PathVariable Long userId) {
        return communityService.getUserModeratorPrivileges(userId);
    }

}
