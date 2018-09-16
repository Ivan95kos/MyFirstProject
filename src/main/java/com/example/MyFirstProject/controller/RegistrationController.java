package com.example.MyFirstProject.controller;

import com.example.MyFirstProject.model.dto.UserDTO;
import com.example.MyFirstProject.model.dto.UserUpdateDTO;
import com.example.MyFirstProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping("/sign-up")
    public void signUp(@RequestBody @Validated UserDTO userDTO) {
        System.out.println(userService.signUp(userDTO));
    }

    @RequestMapping(value = "userUpdate" , method = RequestMethod.POST)
    private void updateUserAccount(@RequestParam(name = "email") String email, @RequestBody @Validated UserUpdateDTO userUpdateDTO){
        userService.updateUser(email, userUpdateDTO);
    }
}
