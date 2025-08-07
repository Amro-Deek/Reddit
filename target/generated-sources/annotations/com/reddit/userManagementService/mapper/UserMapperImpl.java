package com.reddit.userManagementService.mapper;

import com.reddit.userManagementService.controller.dto.request.LoginUserRequest;
import com.reddit.userManagementService.controller.dto.request.PatchUserRequest;
import com.reddit.userManagementService.controller.dto.request.RegisterUserRequest;
import com.reddit.userManagementService.controller.dto.response.LoginUserResponse;
import com.reddit.userManagementService.controller.dto.response.RegisterUserResponse;
import com.reddit.userManagementService.model.User;
import com.reddit.userManagementService.service.dto.request.LoginUserCommand;
import com.reddit.userManagementService.service.dto.request.PatchUserCommand;
import com.reddit.userManagementService.service.dto.request.RegisterUserCommand;
import com.reddit.userManagementService.service.dto.response.LoginUserDTO;
import com.reddit.userManagementService.service.dto.response.RegisterUserDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-08T02:00:25+1400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 24.0.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User fromRegisterUserCommand(RegisterUserCommand registerUserCommand) {
        if ( registerUserCommand == null ) {
            return null;
        }

        User user = new User();

        user.setUsername( registerUserCommand.username() );
        user.setPassword( registerUserCommand.password() );
        user.setEmail( registerUserCommand.email() );

        return user;
    }

    @Override
    public RegisterUserCommand fromRegisterUserRequest(RegisterUserRequest registerUserRequest) {
        if ( registerUserRequest == null ) {
            return null;
        }

        String username = null;
        String email = null;
        String password = null;

        username = registerUserRequest.username();
        email = registerUserRequest.email();
        password = registerUserRequest.password();

        RegisterUserCommand registerUserCommand = new RegisterUserCommand( username, email, password );

        return registerUserCommand;
    }

    @Override
    public RegisterUserDTO toRegisterUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String username = null;
        String email = null;
        String role = null;

        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
        role = user.getRole();

        RegisterUserDTO registerUserDTO = new RegisterUserDTO( id, username, email, role );

        return registerUserDTO;
    }

    @Override
    public RegisterUserResponse toRegisterUserResponse(RegisterUserDTO registerUserDTO) {
        if ( registerUserDTO == null ) {
            return null;
        }

        Long id = null;
        String username = null;
        String email = null;
        String role = null;

        id = registerUserDTO.id();
        username = registerUserDTO.username();
        email = registerUserDTO.email();
        role = registerUserDTO.role();

        RegisterUserResponse registerUserResponse = new RegisterUserResponse( id, username, email, role );

        return registerUserResponse;
    }

    @Override
    public User fromLoginUserCommand(LoginUserCommand loginUserCommand) {
        if ( loginUserCommand == null ) {
            return null;
        }

        User user = new User();

        user.setPassword( loginUserCommand.password() );
        user.setEmail( loginUserCommand.email() );

        return user;
    }

    @Override
    public LoginUserCommand fromLoginUserRequest(LoginUserRequest loginUserRequest) {
        if ( loginUserRequest == null ) {
            return null;
        }

        String email = null;
        String password = null;

        email = loginUserRequest.email();
        password = loginUserRequest.password();

        LoginUserCommand loginUserCommand = new LoginUserCommand( email, password );

        return loginUserCommand;
    }

    @Override
    public LoginUserDTO toLoginUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String username = null;
        String email = null;
        String role = null;

        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
        role = user.getRole();

        LoginUserDTO loginUserDTO = new LoginUserDTO( id, username, email, role );

        return loginUserDTO;
    }

    @Override
    public LoginUserResponse toLoginUserResponse(LoginUserDTO loginUserDTO) {
        if ( loginUserDTO == null ) {
            return null;
        }

        Long id = null;
        String username = null;
        String email = null;
        String role = null;

        id = loginUserDTO.id();
        username = loginUserDTO.username();
        email = loginUserDTO.email();
        role = loginUserDTO.role();

        LoginUserResponse loginUserResponse = new LoginUserResponse( id, username, email, role );

        return loginUserResponse;
    }

    @Override
    public PatchUserCommand fromPatchUserRequest(Long id, PatchUserRequest patchUserRequest) {
        if ( id == null && patchUserRequest == null ) {
            return null;
        }

        String username = null;
        String email = null;
        String password = null;
        if ( patchUserRequest != null ) {
            username = patchUserRequest.username();
            email = patchUserRequest.email();
            password = patchUserRequest.password();
        }
        Long id1 = null;
        id1 = id;

        PatchUserCommand patchUserCommand = new PatchUserCommand( id1, username, email, password );

        return patchUserCommand;
    }
}
