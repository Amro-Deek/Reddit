package com.reddit.userManagementService.controller;


import com.reddit.userManagementService.controller.dto.request.UserIdsRequest;
import com.reddit.userManagementService.controller.dto.response.UserResponse;
import com.reddit.userManagementService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/users")
public class InternalRequestsController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        UserResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/batch")
    public ResponseEntity<List<UserResponse>> getUsersByIds(
            @RequestBody UserIdsRequest listOfUsersInfoRequest,
            @PageableDefault(size = 10) Pageable pageable) {

        List<UserResponse> users = userService.getUsersByIds(listOfUsersInfoRequest.userIds());
        return ResponseEntity.ok(users);
    }
}
