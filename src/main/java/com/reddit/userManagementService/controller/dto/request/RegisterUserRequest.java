package com.reddit.userManagementService.controller.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

public record RegisterUserRequest(
        @Valid String username, String email, String password) {
}
