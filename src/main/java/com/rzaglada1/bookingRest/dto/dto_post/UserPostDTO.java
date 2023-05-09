package com.rzaglada1.bookingRest.dto.dto_post;

import com.rzaglada1.bookingRest.models.enams.Role;
import jakarta.validation.constraints.Email;
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
public class UserPostDTO {
    private long id;

    @Email(message = "{message.error.email-format}")
    @NotBlank(message = "{message.error.notBlank-email}")
    @Length(max = 50, message = "{message.error.length-email}")
    private String email;
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

    @NotBlank(message = "{message.error.notBlank-password}")
    @Length(max = 50, message = "{message.error.length-password}")
    private String password;

    private String passwordOld;

    private Boolean active;


}
