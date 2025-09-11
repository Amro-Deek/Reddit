package com.reddit.coreService.controller.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class CommunityPrivilegesResponse {
    private Long communityId;
    private List<String> privileges;

    public CommunityPrivilegesResponse(Long communityId, List<String> privileges) {
        this.communityId = communityId;
        this.privileges = privileges;
    }
}
