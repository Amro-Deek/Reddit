package com.reddit.userManagementService.controller.dto.request;

public record VerifyUserRequest(
        String email,
         String verificationCode
) {
}
