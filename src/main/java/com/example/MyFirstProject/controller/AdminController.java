package com.example.MyFirstProject.controller;

import com.example.MyFirstProject.model.User;
import com.example.MyFirstProject.model.dto.SingUpDTO;
import com.example.MyFirstProject.service.UserService;
import com.example.MyFirstProject.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messages;

    @PostMapping("/registration")
    public ResponseEntity<GenericResponse> signUp(@RequestBody @Validated final SingUpDTO singUpDTO,
                                                  final HttpServletRequest request) {

        userService.sendEmailActivation(userService.signUpAdmin(singUpDTO), request);

        return new ResponseEntity<>(
                new GenericResponse(messages.getMessage(
                        "message.regSucc",
                        null,
                        request.getLocale()),
                        HttpStatus.OK),
                HttpStatus.OK);
    }

    @DeleteMapping("/users/{usernameOrEmail}")
    public ResponseEntity<GenericResponse> deleteAccount(@PathVariable String username,
                                                         final HttpServletRequest request) {
        userService.delete(userService.findByUsername(username));
        return new ResponseEntity<>(
                new GenericResponse(messages.getMessage(
                        "message.deleteUser",
                        null,
                        request.getLocale()) + username,
                        HttpStatus.OK),
                HttpStatus.OK);
    }

    @GetMapping("/users/{username}")
    public User search(@PathVariable String username) {
        return userService.findByUsername(username);

    }
}