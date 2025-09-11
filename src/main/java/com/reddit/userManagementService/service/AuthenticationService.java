package com.reddit.userManagementService.service;



import com.reddit.userManagementService.client.CoreServiceClient;
import com.reddit.userManagementService.controller.dto.response.CustomUserDetailsResponse;
import com.reddit.userManagementService.mapper.UserMapper;
import com.reddit.userManagementService.model.CommunityPermission;
import com.reddit.userManagementService.model.User;
import com.reddit.userManagementService.repository.UserRepository;
import com.reddit.userManagementService.service.dto.request.LoginUserCommand;
import com.reddit.userManagementService.service.dto.request.RegisterUserCommand;
import com.reddit.userManagementService.service.dto.request.VerifyUserCommand;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private EmailService emailService;
    @Autowired
    private  UserMapper userMapper;
    @Autowired
    private CoreServiceClient coreServiceClient;


    public User signup(@Valid RegisterUserCommand registerUserCommand) {
        User user = new User(registerUserCommand.username(), registerUserCommand.email(), passwordEncoder.encode(registerUserCommand.password()));
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setEnabled(false);
        sendVerificationEmail(user);
        return userRepository.save(user);
    }

//    public User authenticate(@Valid LoginUserCommand loginCommand) {
//        User user = userRepository.findByUsernameAndActiveTrue(loginCommand.username())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (!user.isEnabled()) {
//            throw new RuntimeException("Account not verified. Please verify your account.");
//        }
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginCommand.username(),
//                        loginCommand.password()
//                )
//        );
//
//        return user;
//    }
public CustomUserDetailsResponse authenticate(@Valid LoginUserCommand loginCommand) {
    try {
        // Authenticate using Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginCommand.email(),
                        loginCommand.password()
                )
        );

        // Set the authentication in SecurityContext (important for Spring Security)
        SecurityContextHolder.getContext().setAuthentication(authentication);

    } catch (BadCredentialsException ex) {
        throw new RuntimeException("Invalid email or password"); // or custom exception
    }

    // Load the user manually to fetch additional info/privileges
    User user = userRepository.findByEmailAndActiveTrue(loginCommand.email())
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (!user.isEnabled()) {
        throw new RuntimeException("Account not verified. Please verify your account.");
    }

    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    loginCommand.email(),
                    loginCommand.password()
            )
    );

    // Fetch privileges (Core Service or DB)
    Map<Long, List<CommunityPermission>> privileges =null;
            //= coreServiceClient.getModeratorPrivileges(user.getId());

    // Map User + privileges → CustomUserDetailsResponse
    return userMapper.toCustomUserDetailsResponse(user, privileges);
}


    public void verifyUser(@Valid VerifyUserCommand verifyUserCommand) {
        Optional<User> optionalUser = userRepository.findByEmailAndActiveTrue(verifyUserCommand.email());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has expired !!");
            }
            if (user.getVerificationCode().equals(verifyUserCommand.verificationCode())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid verification code !");
            }
        } else {
            throw new RuntimeException("User with email " + verifyUserCommand.email() + " not found");
        }
    }

    public void resendVerificationCode(@NotBlank @Email String email) {
        Optional<User> optionalUser = userRepository.findByEmailAndActiveTrue(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new RuntimeException("Account is already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(5));
            sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public Long currentUserId(UserDetails principal) {
        // principal.getUsername() must match my unique username in DB
        User me = userRepository.findByUsernameAndActiveTrue(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("Current user not found"));
        return me.getId();
    }

    private void sendVerificationEmail(User user) { //TODO: Update with company logo
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
            //create Custom Exception later
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email to " + user.getEmail());
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
