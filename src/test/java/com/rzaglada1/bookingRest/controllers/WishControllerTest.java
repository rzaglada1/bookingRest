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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class WishControllerTest {

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
    void getWishByIdHouseTest () throws  Exception{
        this.mockMvc
                .perform(get("/wishes/1").header("Authorization", "Bearer " + WishControllerTest.tokenRoleUser))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getWishByIdHouseAndUserNotAuthorizeTest () throws  Exception{
        this.mockMvc
                .perform(get("/wishes/1"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }



    @Test
    void getWishesIfUserAuthorizeTest () throws  Exception{
        this.mockMvc
                .perform(get("/wishes").header("Authorization", "Bearer " + WishControllerTest.tokenRoleUser))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getWishesIfUserNotAuthorizeTest () throws  Exception{
        this.mockMvc
                .perform(get("/wishes") )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }



    public  String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}