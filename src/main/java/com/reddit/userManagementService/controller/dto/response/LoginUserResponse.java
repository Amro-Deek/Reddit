package com.reddit.userManagementService.controller.dto.response;

public record LoginUserResponse( Long id,
         String username,
         String email,
         String role) {
}
