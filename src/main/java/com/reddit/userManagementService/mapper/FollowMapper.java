package com.reddit.userManagementService.mapper;

import com.reddit.userManagementService.controller.dto.response.FollowResponse;
import com.reddit.userManagementService.model.Follower;
import com.reddit.userManagementService.model.User;
import com.reddit.userManagementService.service.dto.response.FollowDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FollowMapper {
    FollowDTO fromUser(User user);
    FollowResponse fromFollowDTO(FollowDTO followDTO);
}
