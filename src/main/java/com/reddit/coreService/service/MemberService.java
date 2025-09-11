package com.reddit.coreService.service;

import com.reddit.coreService.client.UserManagementClient;
import com.reddit.coreService.controller.dto.response.DecideJoinResponse;
import com.reddit.coreService.controller.dto.response.RegisterUserResponse;
import com.reddit.coreService.model.Community;
import com.reddit.coreService.model.CommunityMember;
import com.reddit.coreService.repository.CommunityMemberRepository;
import com.reddit.coreService.repository.CommunityRepository;
import com.reddit.coreService.repository.MemberRepository;
import com.reddit.coreService.service.dto.request.DecideJoinCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final UserManagementClient userClient;
    private final CommunityMemberRepository communityMemberRepository;
    private final CommunityRepository communityRepository;



}
