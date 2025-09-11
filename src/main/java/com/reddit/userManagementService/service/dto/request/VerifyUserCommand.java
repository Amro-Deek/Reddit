package com.reddit.userManagementService.service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyUserCommand (
        @Email @NotBlank String email,
        @NotBlank String verificationCode
){
}
