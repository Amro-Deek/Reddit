package com.reddit.userManagementService.controller;


import com.reddit.userManagementService.controller.dto.response.*;
import com.reddit.userManagementService.mapper.FollowMapper;
import com.reddit.userManagementService.mapper.UserMapper;
import com.reddit.userManagementService.service.FollowService;
import com.reddit.userManagementService.service.dto.response.FollowDTO;
import com.reddit.userManagementService.service.dto.response.RegisterUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/followers")
@RequiredArgsConstructor
public class FollowerController {
    private final FollowService followService;
    private final UserMapper userMapper;
    private final FollowMapper followMapper;

//    @PostMapping("/{followerId}/follow/{followedId}")
//    public ResponseEntity<FollowResponse> follow(@PathVariable Long followerId , @PathVariable Long followedId){
//        FollowDTO followDTO = followService.follow(followerId,followedId);
//        FollowResponse followResponse = followMapper.fromFollowDTO(followDTO);
//        return ResponseEntity.ok(followResponse);
//    }

 //   @PreAuthorize("hasAuthority('13:MANAGE_MEMBERS')")
    @PostMapping("/follow/{followedId}")
    public ResponseEntity<FollowResponse> follow(@PathVariable Long followedId
            , @AuthenticationPrincipal CustomUserDetailsResponse principal){
        // Extract followerId from principal
        Long followerId = principal.getId();
        System.out.println("Authorities: " + principal.getAuthorities());


        FollowDTO followDTO = followService.follow(followerId,followedId);
        FollowResponse followResponse = followMapper.fromFollowDTO(followDTO);
        return ResponseEntity.ok(followResponse);
    }

    @PostMapping("/{followerId}/unfollow/{followedId}")
    public ResponseEntity<FollowResponse> unfollow(@PathVariable Long followerId ,@PathVariable Long followedId){
        FollowDTO followDTO = followService.unfollow(followerId,followedId);
        FollowResponse followResponse = followMapper.fromFollowDTO(followDTO);
        return ResponseEntity.ok(followResponse);
    }

    @GetMapping("{id}/followers")
    public ResponseEntity<PaginatedResponse<FollowResponse>> getFollowers(
            @PathVariable Long id,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable) {

        Page<FollowDTO> dtoPage = followService.getFollowers(id, pageable);

        PaginatedResponse<FollowResponse> body =
                PaginatedResponse.of(dtoPage, followMapper::fromFollowDTO);

        return ResponseEntity.ok(body);
    }

    @GetMapping("{id}/following")
    public ResponseEntity<PaginatedResponse<FollowResponse>> getFollowing(
            @PathVariable Long id,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable) {

        Page<FollowDTO> dtoPage = followService.getFollowing(id, pageable);

        PaginatedResponse<FollowResponse> body =
                PaginatedResponse.of(dtoPage, followMapper::fromFollowDTO);

        return ResponseEntity.ok(body);

    }

    private Sort parseSort(String sort) {
        String[] parts = sort.split(",");
        if (parts.length == 2) {
            return Sort.by(Sort.Direction.fromString(parts[1]), parts[0]);
        }
        return Sort.by(parts[0]).descending();
    }

}
