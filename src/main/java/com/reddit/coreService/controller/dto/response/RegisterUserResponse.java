package com.reddit.coreService.controller.dto.response;

public record RegisterUserResponse(Long id, String username, String email, boolean loggedIn) {
}
