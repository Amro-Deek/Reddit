package com.reddit.userManagementService.service.dto.request;

import java.util.Optional;

public record PatchUserCommand(Long id,
                               String username,
                               String email,
                               String password) {
}
