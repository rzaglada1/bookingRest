package com.rzaglada1.bookingRest.dto.dto_post;

import com.rzaglada1.bookingRest.models.enams.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPostUpdateDTO {
    private long id;

    @NotBlank(message = "{message.error.notBlank-firstName}")
    @Length(max = 50, message = "{message.error.length-firstName}")
    private String firstName;
    @NotBlank(message = "{message.error.notBlank-lastName}")
    @Length(max = 50, message = "{message.error.length-lastName}")
    private String lastName;
    @NotBlank(message = "{message.error.notBlank-phone}")
    @Length(max = 50, message = "{message.error.length-phone}")
    private String phone;

    @NotNull(message = "{message.error.notBlank-role}")
    private Set<Role> roles;

    private String password;
    private String passwordOld;

    @NotNull(message = "{message.error.auth-active-not-null}")
    private Boolean active;
}