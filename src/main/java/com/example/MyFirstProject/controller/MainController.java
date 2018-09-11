package com.example.MyFirstProject.controller;

import com.example.MyFirstProject.model.User;
import com.example.MyFirstProject.model.dto.UserRegistrationDTO;
import com.example.MyFirstProject.model.dto.UserUpdateDTO;
import com.example.MyFirstProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
public class MainController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "registration" , method = RequestMethod.POST)
    public void registerUserAccount(@RequestBody @Validated UserRegistrationDTO userRegistrationDTO) {
        System.out.println(userService.registrationUser(userRegistrationDTO));
    }

    @RequestMapping(value = "userUpdate" , method = RequestMethod.POST)
    private void updateUserAccount(@RequestParam(name = "email") String email, @RequestBody @Validated UserUpdateDTO userUpdateDTO){
        userService.updateUser(email, userUpdateDTO);
    }
}
