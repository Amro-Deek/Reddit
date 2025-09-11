package com.reddit.coreService.security;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Get auth headers

        // Null checks on headers

        // JWT Util extract principle dto, this method throws an error, so it must by handled

        // If no errors and no issues, set the principle in the context







        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String username = jwtService.extractUsername(jwt);
        final Long userId = jwtService.extractUserId(jwt);

        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

        if (username != null ) {
            if (jwtService.isTokenValid(jwt)) {
                JwtAuthentication jwtAuthentication = new JwtAuthentication(userId, username);
                SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
