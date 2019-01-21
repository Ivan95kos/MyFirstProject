package com.example.MyFirstProject.controller;

import com.example.MyFirstProject.MyFirstProjectApplication;
import com.example.MyFirstProject.model.User;
import com.example.MyFirstProject.model.dto.SingInDTO;
import com.example.MyFirstProject.model.dto.SingUpDTO;
import com.example.MyFirstProject.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyFirstProjectApplication.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private static String asJsonString(final Object obj) {

        try {
            return new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void correctSignInTest() throws Exception {

        User user = prepareUser();

        SingInDTO singInDTO = new SingInDTO();
        singInDTO.setUsernameOrEmail(user.getUsername());
        singInDTO.setPassword(user.getPassword());

        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(singInDTO)))
                .andExpect(status().is2xxSuccessful());

        userService.delete(user);
    }

//    @Test
//    public void correctSingUpTest() throws Exception {
//
//        SingUpDTO singUpDTO = new SingUpDTO();
//        singUpDTO.setUsername("usernameTest");
//        singUpDTO.setEmail("emailTest@gmail.com");
//        singUpDTO.
//
//        mockMvc.perform(get("/users/registration")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content())
//                .andExpect(status().is2xxSuccessful());
//    }

    @Test
    public void badSignInTest() throws Exception {

        SingInDTO singInDTO = new SingInDTO();
        singInDTO.setUsernameOrEmail("loginTest");
        singInDTO.setPassword("passwordTest");

        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(singInDTO)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    private User prepareUser() {
        SingUpDTO singUpDTO = new SingUpDTO();
        singUpDTO.setUsername("nameTest");
        singUpDTO.setEmail("emailTest@gmail.com");
        singUpDTO.setPassword("passwordTest");
        User user = userService.activationUser(userService.signUpUser(singUpDTO));
        user.setPassword(singUpDTO.getPassword());
        return user;
    }
}