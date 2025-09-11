package com.reddit.userManagementService.controller.dto.response;

import java.util.List;

public class CommunityPrivilegesResponse {
    private Long communityId;
    private List<String> privileges;

    public Long getCommunityId() { return communityId; }
    public void setCommunityId(Long communityId) { this.communityId = communityId; }

    public List<String> getPrivileges() { return privileges; }
    public void setPrivileges(List<String> privileges) { this.privileges = privileges; }
}