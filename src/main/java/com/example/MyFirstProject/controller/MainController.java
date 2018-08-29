package com.example.MyFirstProject.controller;

import com.example.MyFirstProject.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MainController {

    @RequestMapping("/")
    public String getMainPage() {
        return "index";
    }


    @RequestMapping(value = "registration" , method = RequestMethod.POST)
    public void registerUserAccount(@RequestBody @Validated User user) {
        System.out.println(user);
    }
}
