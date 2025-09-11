package com.reddit.userManagementService.controller.dto.response;

public record RegisterUserResponse(Long id, String username, String email, boolean loggedIn) {
}
