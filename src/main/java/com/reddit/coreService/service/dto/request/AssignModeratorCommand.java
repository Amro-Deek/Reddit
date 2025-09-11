package com.reddit.coreService.service.dto.request;

import com.reddit.coreService.model.PrivilegeName;

import java.util.List;

public record AssignModeratorCommand(long userId  , List<PrivilegeName> privilegeNames) {
}
