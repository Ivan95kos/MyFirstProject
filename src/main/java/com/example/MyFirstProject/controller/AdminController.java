package com.example.MyFirstProject.controller;

import com.example.MyFirstProject.model.User;
import com.example.MyFirstProject.model.dto.SingUpDTO;
import com.example.MyFirstProject.repository.LanguageRepository;
import com.example.MyFirstProject.service.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.AbstractMap;
import java.util.Map;

import static com.example.MyFirstProject.security2.SecurityConstants.HEADER_STRING;
import static com.example.MyFirstProject.security2.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private LanguageRepository languageRepository;

    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 422, message = "Username is already in use"), //
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    @PostMapping("/registration")
    public ResponseEntity<Map.Entry<String, String>> signUp(@RequestBody @Validated final SingUpDTO singUpDTO) {

        String token = userService.signUpAdmin(singUpDTO);

        MultiValueMap<String, String> headers = new HttpHeaders();

        headers.add(HEADER_STRING, TOKEN_PREFIX + token);

        Map.Entry<String, String> tok = new AbstractMap.SimpleEntry<>("token", token);

        return new ResponseEntity<>(tok, headers, HttpStatus.OK);
    }

    @DeleteMapping("/users/{username}")
    public String deleteAccount(@PathVariable String username) {
        User user = userService.findOneByUsernameOrEmail(username);
        userService.delete(user);
        return username;
    }

    @GetMapping("/users/{username}")
    public User search(@PathVariable String username) {

        User user = userService.findOneByUsernameOrEmail(username);

        return user;
    }
}
