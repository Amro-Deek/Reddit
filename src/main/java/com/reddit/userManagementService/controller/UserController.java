package com.reddit.userManagementService.controller;

import com.reddit.userManagementService.controller.dto.request.LoginUserRequest;
import com.reddit.userManagementService.controller.dto.request.PatchUserRequest;
import com.reddit.userManagementService.controller.dto.request.RegisterUserRequest;
import com.reddit.userManagementService.controller.dto.response.LoginUserResponse;
import com.reddit.userManagementService.controller.dto.response.RegisterUserResponse;
import com.reddit.userManagementService.mapper.UserMapper;
import com.reddit.userManagementService.service.UserService;
import com.reddit.userManagementService.service.dto.request.LoginUserCommand;
import com.reddit.userManagementService.service.dto.request.PatchUserCommand;
import com.reddit.userManagementService.service.dto.request.RegisterUserCommand;
import com.reddit.userManagementService.service.dto.response.LoginUserDTO;
import com.reddit.userManagementService.service.dto.response.RegisterUserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(@Valid @RequestBody RegisterUserRequest dto) {
        RegisterUserCommand registerUserCommand = userMapper.fromRegisterUserRequest(dto);
        RegisterUserDTO registerUserDTO = userService.createUser(registerUserCommand);
        RegisterUserResponse registerUserResponse = userMapper.toRegisterUserResponse(registerUserDTO);
        return ResponseEntity.ok(registerUserResponse);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> login(@Valid @RequestBody LoginUserRequest dto) {
        LoginUserCommand loginUserCommand = userMapper.fromLoginUserRequest(dto);
        LoginUserDTO loginUserDTO = userService.login(loginUserCommand);
        LoginUserResponse loginUserResponse = userMapper.toLoginUserResponse(loginUserDTO);
        return ResponseEntity.ok(loginUserResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegisterUserResponse> getUserData(@PathVariable Long id){
        RegisterUserDTO registerUserDTO = userService.getUserByID(id);
        RegisterUserResponse registerUserResponse = userMapper.toRegisterUserResponse(registerUserDTO);
        return ResponseEntity.ok(registerUserResponse);
    }
//
    @PatchMapping("/{id}")
    public ResponseEntity<RegisterUserResponse> patchUser(
            @PathVariable Long id,
            @Valid @RequestBody PatchUserRequest patchUserRequest) {

        PatchUserCommand patchUserCommand = userMapper.fromPatchUserRequest(id,patchUserRequest);
        RegisterUserDTO registerUserDTO = userService.patchUser(patchUserCommand);
        RegisterUserResponse registerUserResponse =userMapper.toRegisterUserResponse(registerUserDTO);
        return ResponseEntity.ok(registerUserResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RegisterUserResponse> deleteUser(@PathVariable Long id ){
        RegisterUserDTO registerUserDTO =userService.deleteUser(id);
        RegisterUserResponse registerUserResponse = userMapper.toRegisterUserResponse(registerUserDTO);
        return ResponseEntity.ok(registerUserResponse);
    }

}
