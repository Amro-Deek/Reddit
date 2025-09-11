package com.reddit.coreService.service.dto.request;

import com.reddit.coreService.model.CommunityType;

public record CreateCommunityCommand(String name,
                                     String description, CommunityType type, Long userId) {
}
