package com.rzaglada1.bookingRest.controllers.error;

import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.NoSuchElementException;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;


@ControllerAdvice
public class AdviceController {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> validationException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Violation(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage()));
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<Object> entityNotFoundException(EntityNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
    }

    @ExceptionHandler(value = NoSuchElementException.class)
    public ResponseEntity<Object> noSuchElementException(NoSuchElementException e) {
        return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
    }


    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalMonitorStateException.class})
    public ResponseEntity<Object> illegalArgument(RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(value = {SignatureException.class})
    public ResponseEntity<Object> signatureException(RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
    }


    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<Object> userNotFoundException(UsernameNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler({ AuthenticationException.class })
    public ResponseEntity<Object> handleAuthenticationException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), UNAUTHORIZED);
    }

}
