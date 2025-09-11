package com.reddit.userManagementService.controller;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reddit.userManagementService.controller.dto.request.LoginUserRequest;
import com.reddit.userManagementService.controller.dto.request.PatchUserRequest;
import com.reddit.userManagementService.controller.dto.request.RegisterUserRequest;
import com.reddit.userManagementService.controller.dto.request.VerifyUserRequest;
import com.reddit.userManagementService.controller.dto.response.*;
import com.reddit.userManagementService.mapper.UserMapper;
import com.reddit.userManagementService.model.User;
import com.reddit.userManagementService.service.AuthenticationService;
import com.reddit.userManagementService.service.JwtService;
import com.reddit.userManagementService.service.UserService;
import com.reddit.userManagementService.service.dto.request.LoginUserCommand;
import com.reddit.userManagementService.service.dto.request.PatchUserCommand;
import com.reddit.userManagementService.service.dto.request.RegisterUserCommand;
import com.reddit.userManagementService.service.dto.response.AllUsersDTO;
import com.reddit.userManagementService.service.dto.response.LoginUserDTO;
import com.reddit.userManagementService.service.dto.response.RegisterUserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/Auth")
@RequiredArgsConstructor
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)

public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;




    /* now we can also add multiple sorts :
    GET /users?sort=id,desc&sort=name,asc
    and add extra field like links later if we need
     */
//    @GetMapping("")
//    public ResponseEntity<PaginatedResponse<AllUsersResponse>> getUsers(
//            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC)
//            Pageable pageable
//    ) {
//        Page<AllUsersDTO> page = userService.getUsers(pageable);
//        PaginatedResponse<AllUsersResponse> body =
//                PaginatedResponse.of(page, userMapper::toAllUsersResponse);
//        return ResponseEntity.ok(body);
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity<RegisterUserResponse> register(@Valid @RequestBody RegisterUserRequest dto) {
//        RegisterUserCommand registerUserCommand = userMapper.fromRegisterUserRequest(dto);
//        RegisterUserDTO registerUserDTO = userService.createUser(registerUserCommand);
//        RegisterUserResponse registerUserResponse = userMapper.toRegisterUserResponse(registerUserDTO);
//        return ResponseEntity.ok(registerUserResponse);
//    }
//
//
////    @PostMapping("/login")
////    public ResponseEntity<LoginUserResponse> login(@Valid @RequestBody LoginUserRequest dto) {
////        LoginUserCommand loginUserCommand = userMapper.fromLoginUserRequest(dto);
////        LoginUserDTO loginUserDTO = userService.login(loginUserCommand);
////        LoginUserResponse loginUserResponse = userMapper.toLoginUserResponse(loginUserDTO);
////        return ResponseEntity.ok(loginUserResponse);
////    }
//
//
//    @PostMapping("/logout/{id}")
//    public ResponseEntity<String> logout(@PathVariable Long id) {
//        userService.logout(id);
//        return ResponseEntity.ok("Logged out .");
//    }
//
    @GetMapping("/{id}")
    public ResponseEntity<RegisterUserResponse> getUserData(@PathVariable Long id){
        RegisterUserDTO registerUserDTO = userService.getUserByID(id);
        RegisterUserResponse registerUserResponse = userMapper.toRegisterUserResponse(registerUserDTO);
        return ResponseEntity.ok(registerUserResponse);
    }
////
//    @PatchMapping("/{id}")
//    public ResponseEntity<RegisterUserResponse> patchUser(
//            @PathVariable Long id,
//            @Valid @RequestBody PatchUserRequest patchUserRequest) {
//
//        PatchUserCommand patchUserCommand = userMapper.fromPatchUserRequest(id,patchUserRequest);
//        RegisterUserDTO registerUserDTO = userService.patchUser(patchUserCommand);
//        RegisterUserResponse registerUserResponse =userMapper.toRegisterUserResponse(registerUserDTO);
//        return ResponseEntity.ok(registerUserResponse);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<RegisterUserResponse> deleteUser(@PathVariable Long id ){
//        userService.deleteUser(id);
//        return ResponseEntity.noContent().build(); // 204, no body
//    }
//
//







    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserRequest registerUserRequest ) {
        User registeredUser = authenticationService.signup
                (userMapper.fromRegisterUserRequest(registerUserRequest));
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> authenticate(@RequestBody LoginUserRequest loginRequest){
        CustomUserDetailsResponse authenticatedUser = authenticationService.authenticate(userMapper.fromLoginUserRequest(loginRequest));

        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginUserResponse loginResponse = new LoginUserResponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserRequest verifyUserRequest) {
        authenticationService.verifyUser(userMapper.toVerifyUserCommand(verifyUserRequest));
        return ResponseEntity.ok("Account verified successfully");
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        authenticationService.resendVerificationCode(email);
        return ResponseEntity.ok("Verification code sent");
    }



}
