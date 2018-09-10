package com.example.MyFirstProject.controller;

import com.example.MyFirstProject.model.User;
import com.example.MyFirstProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MainController {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String getMainPage() {
        return "index";
    }

    @RequestMapping(value = "registration" , method = RequestMethod.POST)
    public void registerUserAccount(@RequestBody @Validated User user) {

        userService.registrationUser(user);

        System.out.println(user);
    }
}
