package com.reddit.userManagementService.mapper;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reddit.userManagementService.controller.dto.request.LoginUserRequest;
import com.reddit.userManagementService.controller.dto.request.PatchUserRequest;
import com.reddit.userManagementService.controller.dto.request.RegisterUserRequest;
import com.reddit.userManagementService.controller.dto.request.VerifyUserRequest;
import com.reddit.userManagementService.controller.dto.response.*;
import com.reddit.userManagementService.model.CommunityPermission;
import com.reddit.userManagementService.model.User;
import com.reddit.userManagementService.service.dto.request.LoginUserCommand;
import com.reddit.userManagementService.service.dto.request.PatchUserCommand;
import com.reddit.userManagementService.service.dto.request.RegisterUserCommand;
import com.reddit.userManagementService.service.dto.request.VerifyUserCommand;
import com.reddit.userManagementService.service.dto.response.AllUsersDTO;
import com.reddit.userManagementService.service.dto.response.LoginUserDTO;
import com.reddit.userManagementService.service.dto.response.RegisterUserDTO;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Map;
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)

@Mapper(componentModel = "spring")
public interface UserMapper {

    User fromRegisterUserCommand(RegisterUserCommand registerUserCommand);
    RegisterUserCommand fromRegisterUserRequest(RegisterUserRequest registerUserRequest);
    RegisterUserDTO toRegisterUserDTO(User user);
    RegisterUserResponse toRegisterUserResponse(RegisterUserDTO registerUserDTO);

    User fromLoginUserCommand(LoginUserCommand loginUserCommand);
    LoginUserCommand fromLoginUserRequest(LoginUserRequest loginUserRequest);
    LoginUserDTO toLoginUserDTO(User user);
    LoginUserResponse toLoginUserResponse(LoginUserDTO loginUserDTO);


    PatchUserCommand fromPatchUserRequest(Long id ,PatchUserRequest patchUserRequest);

    AllUsersResponse toAllUsersResponse(AllUsersDTO allUsersDTO);
    AllUsersDTO toAllUsersDTO(User user);


    @Mapping(target = "authorities", expression = "java(mapAuthorities(user, privilegesPerCommunity))")
    CustomUserDetailsResponse toCustomUserDetailsResponse(User user, Map<Long, List<CommunityPermission>> privilegesPerCommunity);

    default Collection<GrantedAuthority> mapAuthorities(User user, Map<Long, List<CommunityPermission>> privilegesPerCommunity) {
        if (privilegesPerCommunity == null) {
            return List.of();
        }

        return privilegesPerCommunity.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(p -> (GrantedAuthority) new SimpleGrantedAuthority(entry.getKey() + ":" + p.name()))
                )
                .toList();
    }

   VerifyUserCommand toVerifyUserCommand(VerifyUserRequest verifyUserRequest);

    UserResponse toResponse(User user);

    //   CustomUserDetailsResponse toCustomUserDetailsResponse(User user);
}
