package com.reddit.coreService.controller.dto.request;

import com.reddit.coreService.model.PrivilegeName;

import java.util.List;

public record AssignModeratorRequest(long userId , List<PrivilegeName> privilegeNames) {

}
