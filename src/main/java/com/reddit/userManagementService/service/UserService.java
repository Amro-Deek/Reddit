package com.reddit.userManagementService.service;


import com.reddit.userManagementService.controller.dto.response.RegisterUserResponse;
import com.reddit.userManagementService.mapper.UserMapper;
import com.reddit.userManagementService.model.User;
import com.reddit.userManagementService.repository.UserRepository;
import com.reddit.userManagementService.service.dto.request.LoginUserCommand;
import com.reddit.userManagementService.service.dto.request.PatchUserCommand;
import com.reddit.userManagementService.service.dto.request.RegisterUserCommand;
import com.reddit.userManagementService.service.dto.response.LoginUserDTO;
import com.reddit.userManagementService.service.dto.response.RegisterUserDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    public RegisterUserDTO createUser( RegisterUserCommand registerUserCommand) {
        User user = userMapper.fromRegisterUserCommand(registerUserCommand);
        user.setRole("USER");
        User savedUser = userRepository.save(user);
        return userMapper.toRegisterUserDTO(savedUser);
    }

    public LoginUserDTO login(LoginUserCommand loginUserCommand) {
        User user = userRepository.findByEmail(loginUserCommand.email())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.getPassword().equals(loginUserCommand.password())){
            throw new RuntimeException("Invalid credentials");
        }
        return userMapper.toLoginUserDTO(user);

    }

    public RegisterUserDTO getUserByID(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        return userMapper.toRegisterUserDTO(user);
    }

    public RegisterUserDTO patchUser(PatchUserCommand patchUserCommand) {
        User user = userRepository.findById(patchUserCommand.id()).
                orElseThrow(()-> new EntityNotFoundException("User not found"));
        if (patchUserCommand.username()!=null){
user.setUsername(String.valueOf(patchUserCommand.username()));
        }
        if (patchUserCommand.email()!=null){
            user.setEmail(String.valueOf(patchUserCommand.email()));
        }
        if (patchUserCommand.password()!=null){
            user.setPassword(String.valueOf(patchUserCommand.password()));
        }
        User savedUser = userRepository.save(user);
        return userMapper.toRegisterUserDTO(savedUser);

    }

    public RegisterUserDTO deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("User not found"));
        RegisterUserDTO registerUserDTO = userMapper.toRegisterUserDTO(user);
        userRepository.delete(user);
        return registerUserDTO;
    }
}
