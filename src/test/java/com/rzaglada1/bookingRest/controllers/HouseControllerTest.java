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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class HouseControllerTest {

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
    void getHouseByIdTest () throws  Exception{
        this.mockMvc
                .perform(get("/houses/1").header("Authorization", "Bearer " + HouseControllerTest.tokenRoleUser))
                .andDo(print())
                .andExpect(content().json("{\"id\":1}"))
                .andExpect(status().isOk());
    }

    @Test
    void getHouseByIdUserNotAuthorizeTest () throws  Exception{
        this.mockMvc
                .perform(get("/houses/1"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }



    @Test
    void getHousesIfUserAuthorizeTest () throws  Exception{
        this.mockMvc
                        .perform(get("/houses").header("Authorization", "Bearer " + HouseControllerTest.tokenRoleUser))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getHousesIfUserNotAuthorizeTest () throws  Exception{
        this.mockMvc
                .perform(get("/houses") )
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