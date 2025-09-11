package com.reddit.coreService.controller.dto.request;

import com.reddit.coreService.model.CommunityType;
import jakarta.validation.constraints.NotBlank;

public record CreateCommunityRequest(
        @NotBlank(message = "Community name is required")
        String name,
        String description,
        CommunityType type,
        Long userId
) {}
