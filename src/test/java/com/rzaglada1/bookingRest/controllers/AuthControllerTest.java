package com.rzaglada1.bookingRest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void isUserUnauthorizedTest() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @Test
    void isResourceNotFoundTest() throws Exception {
        mockMvc.perform(get("/not_exesting_resource"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void isResourcePermitAllTest() throws Exception {
        Map<String,Object> body = new HashMap<>();
        body.put("country","Україна");
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/find")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(body)))
                .andExpect(status().isOk());
    }


    @Test
    void correctLoginTest () throws  Exception{
        Map<String,Object> body = new HashMap<>();
        body.put("email","1");
        body.put("password","1");
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(body)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)
                );
    }



    private   String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}