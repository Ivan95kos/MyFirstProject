package com.example.MyFirstProject.controller;

import com.example.MyFirstProject.model.dto.UserDTO;
import com.example.MyFirstProject.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class RegistrationController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/sign-up")
    public void signUp(@RequestBody @Validated UserDTO userDTO) {
        System.out.println(userDetailsService.signUp(userDTO));
    }


//    @PreAuthorize("#userId == authentication.user.id && #user != null && #user.id == #userId")
//
//    @RequestMapping(value = "/{userId}/update", method = RequestMethod.POST)
//    private void updateUserAccount(@PathVariable("userId") int userId, @RequestBody @Validated UserUpdateDTO userUpdateDTO) {
//        userService.updateUser(, userUpdateDTO);
//    }
}
