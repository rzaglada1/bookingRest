package com.rzaglada1.bookingRest.services;

import com.rzaglada1.bookingRest.models.User;
import com.rzaglada1.bookingRest.token.JwtService;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class JwtServiceTest {

    @Autowired
    JwtService jwtService;

    static String token;
    @BeforeAll
    static void createToken (@Autowired JwtService jwtService) {
        System.out.println("Create jwt");
        User user = new User();
        user.setEmail("test@user");
        token = jwtService.generateToken(user);
    }



    @Test
    void extractUsernameTest() {
        String expect = "test@user";
        String actual = jwtService.extractUsername(token);
        assertEquals(expect, actual);
    }

    @Test
    void generateTokenTest() {
        User user = new User();
        user.setEmail("test@user");
        String tokenGenerate = jwtService.generateToken(user);

        String expect = "test@user";
        String actual = jwtService.extractUsername(tokenGenerate);
        assertEquals(expect, actual);
    }

    @Test
    void isTokenValidTest() {
        String notValidToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNjg1MjcyMTMyLCJleHAiOjE2ODUzNTg1MzJ9.Q87ed0KshLPkuAqH5i7chiA3tjFTv6Os8ftwEJH9QM8";

        assertThrows(JwtException.class, () -> jwtService.extractUsername(notValidToken));
    }
}