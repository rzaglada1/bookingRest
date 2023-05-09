package com.rzaglada1.bookingRest.dto.dto_get;

import com.rzaglada1.bookingRest.models.enams.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGetDTO {
    private long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Set<Role> roles;
    private LocalDateTime dateCreate;
}
