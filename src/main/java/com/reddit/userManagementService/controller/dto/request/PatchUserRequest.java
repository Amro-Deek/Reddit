package com.reddit.userManagementService.controller.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

public record PatchUserRequest( String username,
                                String email,
                                String password) {
}
