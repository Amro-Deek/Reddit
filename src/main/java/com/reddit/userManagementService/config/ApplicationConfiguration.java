package com.reddit.userManagementService.config;

import com.reddit.userManagementService.client.CoreServiceClient;
import com.reddit.userManagementService.mapper.UserMapper;
import com.reddit.userManagementService.model.CommunityPermission;
import com.reddit.userManagementService.model.User;
import com.reddit.userManagementService.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class ApplicationConfiguration {

    private final UserRepository userRepository;
    private final CoreServiceClient coreServiceClient;
    private final UserMapper userMapper;

    @Bean
    UserDetailsService userDetailsService() {
        return email -> {
            // 1️⃣ Load user by email
            User user = userRepository.findByEmailAndActiveTrue(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

            if (!user.isEnabled()) {
                throw new DisabledException("Account is not verified");
            }

            // 2️⃣ Fetch privileges per community from Core Service
            Map<Long, List<CommunityPermission>> privilegesPerCommunity =null;
                    //= coreServiceClient.getModeratorPrivileges(user.getId());

            if (privilegesPerCommunity == null) {
                privilegesPerCommunity = Map.of(); // avoid null
            }

            // 3️⃣ Map to CustomUserDetailsResponse (implements UserDetails)
            return userMapper.toCustomUserDetailsResponse(user, privilegesPerCommunity);
        };
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}

