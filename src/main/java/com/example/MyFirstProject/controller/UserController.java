package com.example.MyFirstProject.controller;

import com.example.MyFirstProject.model.User;
import com.example.MyFirstProject.model.dto.SingInDTO;
import com.example.MyFirstProject.model.dto.UserUpdateDTO;
import com.example.MyFirstProject.service.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.AbstractMap;
import java.util.Map;

import static com.example.MyFirstProject.security2.SecurityConstants.HEADER_STRING;
import static com.example.MyFirstProject.security2.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

//    @ExceptionHandler({HttpClientErrorException.class})
//    public String handleException() {
//        return "Error";
//    }

    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 422, message = "Invalid username")})
    @PostMapping("/login")
    public ResponseEntity<Map.Entry<String, String>> signIn(@RequestBody @Validated final SingInDTO singInDTO) {

        String token = userService.signIn(singInDTO);

        MultiValueMap<String, String> headers = new HttpHeaders();

        headers.add(HEADER_STRING, TOKEN_PREFIX + token);

        Map.Entry<String, String> tok = new AbstractMap.SimpleEntry<>("token", token);

        return new ResponseEntity<>(tok, headers, HttpStatus.OK);
    }

//    @ApiResponses(value = {//
//            @ApiResponse(code = 400, message = "Something went wrong"), //
//            @ApiResponse(code = 403, message = "Access denied"), //
//            @ApiResponse(code = 422, message = "Username is already in use"), //
//            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
//    @PostMapping("/registration")
//    public ResponseEntity<Map.Entry<String, String>> signUp(@RequestBody @Validated final SingUpDTO userDTO) {
//
//        String token = userService.signUpUser(userDTO);
//
//        MultiValueMap<String, String> headers = new HttpHeaders();
//
//        headers.add(HEADER_STRING, TOKEN_PREFIX + token);
//
//        Map.Entry<String, String> tok = new AbstractMap.SimpleEntry<>("token", token);
//
//        return new ResponseEntity<>(tok, headers, HttpStatus.OK);
//    }

    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "The user doesn't exist"), //
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    @PatchMapping("/myself")
    public String updateAccount(@RequestBody @Validated final UserUpdateDTO userUpdateDTO, final Authentication authentication) {

        User user = userService.findOneByUsernameOrEmail(authentication.getName());

        userService.updateAccount(user, userUpdateDTO);

        return "successfully";
    }

    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    @GetMapping("/me")
    public User whoAmI(final Authentication authentication) {

        User user = userService.findOneByUsernameOrEmail(authentication.getName());

        return user;
    }

}
