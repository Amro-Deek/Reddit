package com.reddit.coreService.mapper;

import com.reddit.coreService.controller.dto.request.CreatePrivilegeRequest;
import com.reddit.coreService.controller.dto.response.CreatePrivilegeResponse;
import com.reddit.coreService.model.Privilege;
import com.reddit.coreService.service.dto.request.CreatePrivilegeCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PrivilegeMapper {
    CreatePrivilegeCommand fromCreatePrivilegeRequest(CreatePrivilegeRequest createPrivilegeRequest);
    CreatePrivilegeResponse fromPrivilege(Privilege privilege);

}
