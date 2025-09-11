package com.reddit.userManagementService.controller.dto.response;

public record LoginUserResponse( String token,
                                 long expiresIn) {
}
