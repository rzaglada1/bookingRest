package com.rzaglada1.bookingRest.token;

import com.rzaglada1.bookingRest.models.enams.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private long id;
    private String email;
    private String firstName;
    private String lastName;
    private Set<Role> role;
    private String accessToken;
}
