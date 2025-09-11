package com.reddit.userManagementService.controller.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


public record UserResponse(
        @NotBlank long id,
        @NotBlank String username,
        @Email String email
) {
}
