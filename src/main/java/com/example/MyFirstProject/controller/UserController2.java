package com.example.MyFirstProject.controller;

import com.example.MyFirstProject.model.User;
import com.example.MyFirstProject.model.dto.SingUpDTO;
import com.example.MyFirstProject.security2.JwtTokenProvider;
import com.example.MyFirstProject.service.EmailService;
import com.example.MyFirstProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.AbstractMap;
import java.util.Map;

import static com.example.MyFirstProject.security2.SecurityConstants.HEADER_STRING;
import static com.example.MyFirstProject.security2.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/users")
public class UserController2 {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/registration")
    public ResponseEntity<String> singUpUser(@RequestBody @Validated SingUpDTO singUpDTO, HttpServletRequest request){

        String token = userService.signUpUser(singUpDTO);

        String appUrl = request.getScheme() + "://" + request.getServerName();

        SimpleMailMessage registrationEmail = new SimpleMailMessage();
        registrationEmail.setTo(singUpDTO.getEmail());
        registrationEmail.setSubject("Registration Confirmation");
        registrationEmail.setText("To confirm your e-mail address, please click the link below:\n"
                + appUrl + "/users/activate?token=" + token);

        emailService.sendEmail(registrationEmail);

        return new ResponseEntity<>("{success:true}", HttpStatus.OK) ;

    }

    @PostMapping("/activate")
    public ResponseEntity<Map.Entry<String, String>> processConfirmationForm(@RequestParam("token") String token){

        User user = userService.findOneByUsernameOrEmail(jwtTokenProvider.getUsername(token));

        user.setEnabled(true);

        userService.saveAndFlush(user);

        MultiValueMap<String, String> headers = new HttpHeaders();

        headers.add(HEADER_STRING, TOKEN_PREFIX + token);

        Map.Entry<String, String> tok = new AbstractMap.SimpleEntry<>("token", token);

        return new ResponseEntity<>(tok, headers, HttpStatus.OK);
    }
}
