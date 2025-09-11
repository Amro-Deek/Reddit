package com.reddit.coreService.controller.dto.response;

import com.reddit.coreService.model.PrivilegeName;

import java.util.List;

public record CommunityModeratorsPrivilegesResponse(Long id,
                                                    Long userId,
                                                    String communityName,
                                                    List<PrivilegeName> privileges) {
}
