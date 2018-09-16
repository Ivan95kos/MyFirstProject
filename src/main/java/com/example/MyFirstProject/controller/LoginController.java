package com.example.MyFirstProject.controller;

import com.example.MyFirstProject.model.User;
import com.example.MyFirstProject.model.dto.UserDTO;
import com.example.MyFirstProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping
    public void LoginUser(@RequestBody UserDTO userDTO) {

        User user = userService.findByUsername(userDTO.getUsername());

        if (user.getPassword().equals(userDTO.getPassword())){
            System.out.println(user.getPassword() + "  OK");
        } else {
            System.out.println("Bad usernameOrEmail or password");
        }
    }
}
