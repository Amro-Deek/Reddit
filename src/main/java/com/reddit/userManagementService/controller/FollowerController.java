package com.reddit.userManagementService.controller;


import com.reddit.userManagementService.controller.dto.response.FollowResponse;
import com.reddit.userManagementService.controller.dto.response.PageResponse;
import com.reddit.userManagementService.controller.dto.response.RegisterUserResponse;
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
    public ResponseEntity<PageResponse<FollowResponse>> getFollowers(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {

        Sort sortObj = parseSort(sort);

        Pageable pageable = PageRequest.of(page, size, sortObj);

        Page<FollowDTO> dtoPage = followService.getFollowers(id, pageable);

        Page<FollowResponse> followResponsePage = dtoPage.map(followMapper::fromFollowDTO);

        PageResponse<FollowResponse> pageResponse = PageResponse.from(followResponsePage);

        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("{id}/following")
    public ResponseEntity<PageResponse<FollowResponse>> getFollowing(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {

        Sort sortObj = parseSort(sort);
        Pageable pageable = PageRequest.of(page, size, sortObj);



        Page<FollowDTO> dtoPage = followService.getFollowing(id, pageable);

        Page<FollowResponse> followResponsePage = dtoPage.map(followMapper::fromFollowDTO);

        PageResponse<FollowResponse> pageResponse = PageResponse.from(followResponsePage);

        return ResponseEntity.ok(pageResponse);

    }

    private Sort parseSort(String sort) {
        String[] parts = sort.split(",");
        if (parts.length == 2) {
            return Sort.by(Sort.Direction.fromString(parts[1]), parts[0]);
        }
        return Sort.by(parts[0]).descending();
    }

}
