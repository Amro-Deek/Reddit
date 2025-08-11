package com.reddit.userManagementService.mapper;


import com.reddit.userManagementService.controller.dto.request.LoginUserRequest;
import com.reddit.userManagementService.controller.dto.request.PatchUserRequest;
import com.reddit.userManagementService.controller.dto.request.RegisterUserRequest;
import com.reddit.userManagementService.controller.dto.response.AllUsersResponse;
import com.reddit.userManagementService.controller.dto.response.LoginUserResponse;
import com.reddit.userManagementService.controller.dto.response.RegisterUserResponse;
import com.reddit.userManagementService.model.User;
import com.reddit.userManagementService.service.dto.request.LoginUserCommand;
import com.reddit.userManagementService.service.dto.request.PatchUserCommand;
import com.reddit.userManagementService.service.dto.request.RegisterUserCommand;
import com.reddit.userManagementService.service.dto.response.AllUsersDTO;
import com.reddit.userManagementService.service.dto.response.LoginUserDTO;
import com.reddit.userManagementService.service.dto.response.RegisterUserDTO;
import org.mapstruct.Mapper;

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
}
