package com.reddit.coreService.service.dto.request;

import com.reddit.coreService.model.PrivilegeName;

public record CreatePrivilegeCommand(PrivilegeName name , String description) {
}
