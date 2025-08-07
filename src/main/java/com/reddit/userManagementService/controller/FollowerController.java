package com.reddit.userManagementService.controller;


import com.reddit.userManagementService.controller.dto.response.FollowResponse;
import com.reddit.userManagementService.controller.dto.response.RegisterUserResponse;
import com.reddit.userManagementService.mapper.FollowMapper;
import com.reddit.userManagementService.mapper.UserMapper;
import com.reddit.userManagementService.service.FollowService;
import com.reddit.userManagementService.service.dto.response.FollowDTO;
import com.reddit.userManagementService.service.dto.response.RegisterUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowerController {
    private final FollowService followService;
    private final UserMapper userMapper;
    private final FollowMapper followMapper;

    @PostMapping("/{followerId}/follow/{followedId}")
    public ResponseEntity<FollowResponse> follow(@PathVariable Long followerId , @PathVariable Long followedId){
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
    public ResponseEntity<List<FollowResponse>> getFollowers(@PathVariable Long id){
        List<FollowDTO> followDTOS = followService.getFollowers(id);

        return ResponseEntity.ok(followDTOS.stream()
                .map(f ->followMapper.fromFollowDTO(f)).toList());
    }

    @GetMapping("{id}/following")
    public ResponseEntity<List<FollowResponse>> getFollowing(@PathVariable Long id){
        List<FollowDTO> followDTOS = followService.getFollowing(id);

        return ResponseEntity.ok(followDTOS.stream()
                .map(f ->followMapper.fromFollowDTO(f)).toList());
    }

}
