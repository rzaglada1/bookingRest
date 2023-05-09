package com.rzaglada1.bookingRest.models.enams;

import org.springframework.security.core.GrantedAuthority;


public enum Role implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_USER,
    ROLE_GUEST;

    @Override
    public String getAuthority() {
        return name();
    }
}
