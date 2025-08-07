package com.reddit.userManagementService.service.dto.response;

public record LoginUserDTO( Long id,
                            String username,
                            String email,
                            String role) {
}
