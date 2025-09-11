package com.reddit.userManagementService.mapper;

import com.reddit.userManagementService.controller.dto.request.LoginUserRequest;
import com.reddit.userManagementService.controller.dto.request.PatchUserRequest;
import com.reddit.userManagementService.controller.dto.request.RegisterUserRequest;
import com.reddit.userManagementService.controller.dto.request.VerifyUserRequest;
import com.reddit.userManagementService.controller.dto.response.AllUsersResponse;
import com.reddit.userManagementService.controller.dto.response.CustomUserDetailsResponse;
import com.reddit.userManagementService.controller.dto.response.LoginUserResponse;
import com.reddit.userManagementService.controller.dto.response.RegisterUserResponse;
import com.reddit.userManagementService.controller.dto.response.UserResponse;
import com.reddit.userManagementService.model.CommunityPermission;
import com.reddit.userManagementService.model.User;
import com.reddit.userManagementService.service.dto.request.LoginUserCommand;
import com.reddit.userManagementService.service.dto.request.PatchUserCommand;
import com.reddit.userManagementService.service.dto.request.RegisterUserCommand;
import com.reddit.userManagementService.service.dto.request.VerifyUserCommand;
import com.reddit.userManagementService.service.dto.response.AllUsersDTO;
import com.reddit.userManagementService.service.dto.response.LoginUserDTO;
import com.reddit.userManagementService.service.dto.response.RegisterUserDTO;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-12T03:07:01+1400",
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
        boolean loggedIn = false;

        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
        loggedIn = user.isLoggedIn();

        String role = null;

        RegisterUserDTO registerUserDTO = new RegisterUserDTO( id, username, email, role, loggedIn );

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
        boolean loggedIn = false;

        id = registerUserDTO.id();
        username = registerUserDTO.username();
        email = registerUserDTO.email();
        loggedIn = registerUserDTO.loggedIn();

        RegisterUserResponse registerUserResponse = new RegisterUserResponse( id, username, email, loggedIn );

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
        boolean loggedIn = false;

        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
        loggedIn = user.isLoggedIn();

        String role = null;

        LoginUserDTO loginUserDTO = new LoginUserDTO( id, username, email, role, loggedIn );

        return loginUserDTO;
    }

    @Override
    public LoginUserResponse toLoginUserResponse(LoginUserDTO loginUserDTO) {
        if ( loginUserDTO == null ) {
            return null;
        }

        String token = null;
        long expiresIn = 0L;

        LoginUserResponse loginUserResponse = new LoginUserResponse( token, expiresIn );

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

    @Override
    public AllUsersResponse toAllUsersResponse(AllUsersDTO allUsersDTO) {
        if ( allUsersDTO == null ) {
            return null;
        }

        Long id = null;
        String username = null;
        String email = null;
        String role = null;

        id = allUsersDTO.id();
        username = allUsersDTO.username();
        email = allUsersDTO.email();
        role = allUsersDTO.role();

        AllUsersResponse allUsersResponse = new AllUsersResponse( id, username, email, role );

        return allUsersResponse;
    }

    @Override
    public AllUsersDTO toAllUsersDTO(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String username = null;
        String email = null;

        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();

        String role = null;

        AllUsersDTO allUsersDTO = new AllUsersDTO( id, username, email, role );

        return allUsersDTO;
    }

    @Override
    public CustomUserDetailsResponse toCustomUserDetailsResponse(User user, Map<Long, List<CommunityPermission>> privilegesPerCommunity) {
        if ( user == null && privilegesPerCommunity == null ) {
            return null;
        }

        User user1 = null;
        user1 = user;

        Collection<? extends GrantedAuthority> authorities = mapAuthorities(user, privilegesPerCommunity);

        CustomUserDetailsResponse customUserDetailsResponse = new CustomUserDetailsResponse( user1, authorities );

        return customUserDetailsResponse;
    }

    @Override
    public VerifyUserCommand toVerifyUserCommand(VerifyUserRequest verifyUserRequest) {
        if ( verifyUserRequest == null ) {
            return null;
        }

        String email = null;
        String verificationCode = null;

        email = verifyUserRequest.email();
        verificationCode = verifyUserRequest.verificationCode();

        VerifyUserCommand verifyUserCommand = new VerifyUserCommand( email, verificationCode );

        return verifyUserCommand;
    }

    @Override
    public UserResponse toResponse(User user) {
        if ( user == null ) {
            return null;
        }

        long id = 0L;
        String username = null;
        String email = null;

        if ( user.getId() != null ) {
            id = user.getId();
        }
        username = user.getUsername();
        email = user.getEmail();

        UserResponse userResponse = new UserResponse( id, username, email );

        return userResponse;
    }
}
