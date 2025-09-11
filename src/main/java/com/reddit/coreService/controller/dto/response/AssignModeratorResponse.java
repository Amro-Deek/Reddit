package com.reddit.coreService.controller.dto.response;

import com.reddit.coreService.model.PrivilegeName;

import java.util.List;

public record AssignModeratorResponse(long newModeratorId  , long communityId , List<PrivilegeName> privilegeNames) {
}
