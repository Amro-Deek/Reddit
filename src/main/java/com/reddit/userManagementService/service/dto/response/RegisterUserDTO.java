package com.reddit.userManagementService.service.dto.response;

public record RegisterUserDTO(Long id, String username, String email, String role, boolean loggedIn) {
}
