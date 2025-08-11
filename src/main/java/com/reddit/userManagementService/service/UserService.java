package com.reddit.userManagementService.service;


import com.reddit.userManagementService.controller.dto.response.RegisterUserResponse;
import com.reddit.userManagementService.mapper.UserMapper;
import com.reddit.userManagementService.model.User;
import com.reddit.userManagementService.repository.UserRepository;
import com.reddit.userManagementService.service.dto.request.LoginUserCommand;
import com.reddit.userManagementService.service.dto.request.PatchUserCommand;
import com.reddit.userManagementService.service.dto.request.RegisterUserCommand;
import com.reddit.userManagementService.service.dto.response.AllUsersDTO;
import com.reddit.userManagementService.service.dto.response.LoginUserDTO;
import com.reddit.userManagementService.service.dto.response.RegisterUserDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    public RegisterUserDTO createUser( RegisterUserCommand registerUserCommand) {

        if (userRepository.existsByEmailAndActiveTrue(registerUserCommand.email())) {
            throw new RuntimeException("Email is already in use by an active account.");
        }
        User user = userMapper.fromRegisterUserCommand(registerUserCommand);
        user.setRole("USER");
        user.setActive(true);
        User savedUser = userRepository.save(user);
        return userMapper.toRegisterUserDTO(savedUser);
    }

    public LoginUserDTO login(LoginUserCommand loginUserCommand) {
        User user = userRepository.findByEmailAndActiveTrue(loginUserCommand.email())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.getPassword().equals(loginUserCommand.password())){
            throw new RuntimeException("Invalid credentials");
        }
        return userMapper.toLoginUserDTO(user);

    }

    public RegisterUserDTO getUserByID(Long id) {
        User user = userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        return userMapper.toRegisterUserDTO(user);
    }

    public RegisterUserDTO patchUser(PatchUserCommand patchUserCommand) {
        User user = userRepository.findByIdAndActiveTrue(patchUserCommand.id()).
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
        User user = userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(()-> new EntityNotFoundException("User not found"));
        RegisterUserDTO registerUserDTO = userMapper.toRegisterUserDTO(user);
        user.setActive(false);
        userRepository.save(user);
        return registerUserDTO;
    }

    public Page<AllUsersDTO> getUsers(Pageable pageable) {
        Page<User> users = userRepository.findByActiveTrue(pageable);
        return users.map(userMapper::toAllUsersDTO);
    }
}
