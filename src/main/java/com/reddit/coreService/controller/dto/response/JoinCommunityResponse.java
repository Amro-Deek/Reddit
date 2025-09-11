package com.reddit.coreService.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinCommunityResponse {
    private Long userId;
    private Long communityId;
    private String message;
}