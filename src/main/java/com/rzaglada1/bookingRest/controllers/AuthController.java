package com.rzaglada1.bookingRest.controllers;


import com.rzaglada1.bookingRest.models.User;
import com.rzaglada1.bookingRest.token.AuthRequest;
import com.rzaglada1.bookingRest.token.AuthResponse;
import com.rzaglada1.bookingRest.token.JwtService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;


@AllArgsConstructor
@RestController
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;


    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request, BindingResult bindingResult) {

        System.out.println("request " + request);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapErrors(bindingResult));
        }

        try {
            System.out.println("request " + request);
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword())
            );
            System.out.println("111");

            User user = (User) authentication.getPrincipal();
            String accessToken = jwtService.generateToken(user);
            jwtService.revokeAllUserTokens(user);
            jwtService.saveUserToken(user, accessToken);

            AuthResponse response = new AuthResponse(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getRoles(), accessToken);

           return ResponseEntity.ok().body(response);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }





    private Map<String, String> mapErrors (BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream().collect(
                Collectors.toMap(fieldError -> fieldError.getField() + "Error", f-> {
                    if (f.getDefaultMessage() != null) {
                        return f.getDefaultMessage();
                    }
                    return f.getDefaultMessage();
                }));
    }

}

