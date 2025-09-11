package com.reddit.userManagementService.service.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Optional;
public record PatchUserCommand(Long id,
                               String username,
                               String email,
                               String password) {
}
