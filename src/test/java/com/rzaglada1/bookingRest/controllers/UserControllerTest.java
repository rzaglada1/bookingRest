package com.rzaglada1.bookingRest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rzaglada1.bookingRest.models.User;
import com.rzaglada1.bookingRest.token.JwtService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    static String tokenRoleUser;
    static String tokenRoleAdmin;
    @BeforeAll
            static void createToken (@Autowired JwtService jwtService) {
        System.out.println("Create jwt");
        User user = new User();
        user.setEmail("test@user");
        user.setId(57);

        tokenRoleUser = jwtService.generateToken(user);
        jwtService.revokeAllUserTokens(user);
        jwtService.saveUserToken(user, tokenRoleUser);

        user.setEmail("test@admin");
        user.setId(58);
        tokenRoleAdmin = jwtService.generateToken(user);
        jwtService.revokeAllUserTokens(user);
        jwtService.saveUserToken(user, tokenRoleAdmin);
    }



    @Test
    void createNewUserIsDoubleEmailTest () throws Exception {
        Map<String,Object> body = new HashMap<>();
        body.put("email","test@user");
        body.put("firstName","test User");
        body.put("lastName","test User");
        body.put("phone","+380987676545");
        body.put("password","password");
        this.mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(body))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    void getUserByIdIfUserNotAdminTest () throws  Exception{
        this.mockMvc
                .perform(get("/users/57").header("Authorization", "Bearer " + tokenRoleUser) )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getUserByIdIfUserAdminTest () throws  Exception{
        this.mockMvc
                .perform(get("/users/3").header("Authorization", "Bearer " + tokenRoleAdmin) )
                .andExpect(status().isOk());
    }

    @Test
    void getUserByIdIfUserNotAdminAndAnotherIdTest () throws  Exception{
        this.mockMvc
                .perform(get("/users/5").header("Authorization", "Bearer " + UserControllerTest.tokenRoleUser) )
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUsersIfUserNotAdminTest () throws  Exception{
        this.mockMvc
                .perform(get("/users").header("Authorization", "Bearer " + UserControllerTest.tokenRoleUser) )
                .andExpect(status().isForbidden());
    }

    @Test
    void getUsersIfUserAdminTest () throws  Exception{
        this.mockMvc
                .perform(get("/users").header("Authorization", "Bearer " + UserControllerTest.tokenRoleAdmin) )
                .andExpect(status().isOk());
    }

    @Test
    void getUserParamTest () throws  Exception{
        this.mockMvc
                .perform(get("/users/param").header("Authorization", "Bearer " + UserControllerTest.tokenRoleUser) )
                .andExpect(status().isOk());
    }


    public  String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}