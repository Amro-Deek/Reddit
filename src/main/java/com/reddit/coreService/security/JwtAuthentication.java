package com.reddit.coreService.security;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@AllArgsConstructor
public class JwtAuthentication implements Authentication {

    private final Long userId;
    private final String username;

    // Not needed, but required by interface
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this; // So you can inject this with @AuthenticationPrincipal
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        // Do nothing
    }

    @Override
    public String getName() {
        return username;
    }
}
