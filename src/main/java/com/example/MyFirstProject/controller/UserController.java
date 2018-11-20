package com.example.MyFirstProject.controller;

import com.example.MyFirstProject.model.User;
import com.example.MyFirstProject.model.dto.PasswordDTO;
import com.example.MyFirstProject.model.dto.SingInDTO;
import com.example.MyFirstProject.model.dto.SingUpDTO;
import com.example.MyFirstProject.model.dto.UserUpdateDTO;
import com.example.MyFirstProject.security2.JwtTokenProvider;
import com.example.MyFirstProject.service.UserService;
import com.example.MyFirstProject.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MessageSource messages;

//    @ExceptionHandler({CustomException.class})
//    public String handleException() {
//        return "error";
//    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponse> signIn(@RequestBody @Validated final SingInDTO singInDTO) {

        return userService.getResponseEntityWithToken(jwtTokenProvider.createToken(userService.signIn(singInDTO)));

    }

    @PostMapping("/registration")
    public ResponseEntity<GenericResponse> singUpUser(@RequestBody @Validated final SingUpDTO singUpDTO,
                                                      final HttpServletRequest request) {

        userService.sendEmailActivation(
                jwtTokenProvider.createToken(userService.signUpUser(singUpDTO)),
                singUpDTO.getEmail(),
                request);

        return getGenericResponseResponseEntity(request, "message.regSucc");
    }

    @GetMapping("/activate")
    public ResponseEntity<GenericResponse> processConfirmationForm(@RequestParam("token") final String token) {

        jwtTokenProvider.validateToken(token);

        return userService.getResponseEntityWithToken(
                jwtTokenProvider.createToken(
                        userService.activationUser(
                                userService.findByUsername(
                                        jwtTokenProvider.getUsername(token)))));

    }

    @PatchMapping("/myself")
    public ResponseEntity<GenericResponse> updateAccount(@RequestBody @Validated final UserUpdateDTO userUpdateDTO,
                                                         final Authentication authentication,
                                                         final HttpServletRequest request) {

        userService.updateAccount(userService.findByUsername(authentication.getName()), userUpdateDTO);

        return getGenericResponseResponseEntity(request, "message.successfully");
    }

    @GetMapping("/me")
    public User whoAmI(final Authentication authentication) {

        return userService.findOneByUsernameOrEmail(authentication.getName());

    }

    @PostMapping("/resetPassword")
    public ResponseEntity<GenericResponse> resetPassword(final HttpServletRequest request,
                                                         @RequestParam("email") final String email) {
        userService.resetPassword(email, request);

        return getGenericResponseResponseEntity(request, "message.resetPasswordEmail");
    }

    @GetMapping("/changePassword")
    public ResponseEntity<GenericResponse> showChangePasswordPage(@RequestParam("token") final String token) {

        jwtTokenProvider.validateToken(token);

        return userService.getResponseEntityWithToken(
                jwtTokenProvider.createToken(
                        userService.activationUser(
                                userService.findByUsername(
                                        jwtTokenProvider.getUsername(token)))));
    }

    @PostMapping("/savePassword")
    public ResponseEntity<GenericResponse> savePassword(@RequestBody @Validated final PasswordDTO passwordDTO,
                                                        final Authentication authentication,
                                                        final HttpServletRequest request) {

        userService.savePassword(authentication.getName(), passwordDTO.getNewPassword());
        return getGenericResponseResponseEntity(request, "message.successfully");
    }

    // ============== NON-API ============

    private ResponseEntity<GenericResponse> getGenericResponseResponseEntity(HttpServletRequest request,
                                                                             String message) {
        return new ResponseEntity<>(
                new GenericResponse(messages.getMessage(
                        message,
                        null,
                        request.getLocale()),
                        HttpStatus.OK),
                HttpStatus.OK);
    }
}