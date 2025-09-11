package com.reddit.userManagementService.service;
import com.reddit.userManagementService.client.CoreServiceClient;
import com.reddit.userManagementService.controller.dto.response.CustomUserDetailsResponse;
import com.reddit.userManagementService.mapper.UserMapper;
import com.reddit.userManagementService.model.CommunityPermission;
import com.reddit.userManagementService.model.User;
import com.reddit.userManagementService.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CoreServiceClient coreServiceClient;
    private final UserMapper userMapper;


    @Override
    public CustomUserDetailsResponse loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndActiveTrue(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        Map<Long, List<CommunityPermission>> privileges = coreServiceClient.getModeratorPrivileges(user.getId());

        return userMapper.toCustomUserDetailsResponse(user, privileges);
    }

}