package com.reddit.userManagementService.controller.dto.request;

public record PatchUserRequest( String username,
                                String email,
                                String password) {
}
