package com.reddit.coreService.controller.dto.response;

import com.reddit.coreService.model.CommunityType;

public record CommunityResponse(String name,
                                String description, CommunityType type) {
}
