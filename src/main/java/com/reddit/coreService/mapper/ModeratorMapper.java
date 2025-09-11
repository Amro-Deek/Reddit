package com.reddit.coreService.mapper;

import com.reddit.coreService.controller.dto.request.AssignModeratorRequest;
import com.reddit.coreService.controller.dto.response.CommunityModeratorsPrivilegesResponse;
import com.reddit.coreService.model.Moderator;
import com.reddit.coreService.model.ModeratorPrivilege;
import com.reddit.coreService.model.PrivilegeName;
import com.reddit.coreService.service.dto.request.AssignModeratorCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ModeratorMapper {
    AssignModeratorCommand toAssignModeratorCommand(AssignModeratorRequest assignModeratorRequest);

//    @Mapping(target = "id", source = "moderator.id")
//    @Mapping(target = "userId", source = "moderator.userId")
//    @Mapping(target = "communityName", source = "moderator.community.name")
//    @Mapping(target = "privileges", expression = "java(mapPrivileges(privileges))")
    CommunityModeratorsPrivilegesResponse toResponse(Moderator moderator, List<ModeratorPrivilege> privileges);

    default List<PrivilegeName> mapPrivileges(List<ModeratorPrivilege> privileges) {
        if (privileges == null) return List.of();
        return privileges.stream()
                .map(mp -> mp.getPrivilege().getName())
                .toList();
    }
}
