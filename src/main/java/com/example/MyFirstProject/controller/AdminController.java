package com.example.MyFirstProject.controller;

import com.example.MyFirstProject.model.User;
import com.example.MyFirstProject.model.dto.SingUpDTO;
import com.example.MyFirstProject.service.EmailService;
import com.example.MyFirstProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/registration")
    public ResponseEntity<String> signUp(@RequestBody @Validated final SingUpDTO singUpDTO, HttpServletRequest request) {

        String token = userService.signUpAdmin(singUpDTO);

        String appUrl = request.getScheme() + "://" + request.getServerName();

        SimpleMailMessage registrationEmail = new SimpleMailMessage();
        registrationEmail.setTo(singUpDTO.getEmail());
        registrationEmail.setSubject("Registration Confirmation");
        registrationEmail.setText("To confirm your e-mail address, please click the link below:\n"
                + appUrl + "/users/activate?token=" + token);

        emailService.sendEmail(registrationEmail);

        return new ResponseEntity<>("{success:true}", HttpStatus.OK) ;
    }

    @DeleteMapping("/users/{username}")
    public String deleteAccount(@PathVariable String username) {
        User user = userService.findOneByUsernameOrEmail(username);
        userService.delete(user);
        return username;
    }

    @GetMapping("/users/{username}")
    public User search(@PathVariable String username) {

        return userService.findOneByUsernameOrEmail(username);

    }
}
