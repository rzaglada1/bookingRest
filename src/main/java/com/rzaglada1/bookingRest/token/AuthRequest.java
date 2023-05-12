package com.rzaglada1.bookingRest.token;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
     @NotNull (message = "{message.error.auth-email-notBlank}")
//    @Email(message = "{message.error.auth-email-format}")
    @Length(min = 1, max = 50, message = "{message.error.auth-email-length}")
    private String email;

    @NotNull (message = "{message.error.auth-password-notBlank}")
    @Length(min = 1, max = 10, message = "{message.error.auth-password-length}")
    private String password;
}
