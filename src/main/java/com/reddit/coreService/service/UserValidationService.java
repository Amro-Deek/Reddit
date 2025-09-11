package com.reddit.coreService.service;

import com.reddit.coreService.client.UserManagementClient;
import com.reddit.coreService.controller.dto.response.RegisterUserResponse;
import com.reddit.coreService.controller.dto.response.UserResponse;
import com.reddit.coreService.exceptions.ResourceNotFoundException;
import com.reddit.coreService.exceptions.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValidationService {

    private final UserManagementClient userClient;


    public UserResponse validateUserExistsAndLoggedIn(Long userId) {
        UserResponse user;
        try {
            user = userClient.getUserById(userId);
        } catch (Exception e) {
            throw new ResourceNotFoundException("User with ID " + userId + " not found");
        }

        if (user == null) {
            throw new ResourceNotFoundException("User with ID " + userId + " not found");
        }



        return user;
    }

}

