package com.example.MyFirstProject.controller;

import com.example.MyFirstProject.model.User;
import com.example.MyFirstProject.model.dto.PasswordDTO;
import com.example.MyFirstProject.model.dto.SingInDTO;
import com.example.MyFirstProject.model.dto.SingUpDTO;
import com.example.MyFirstProject.model.dto.UserUpdateDTO;
import com.example.MyFirstProject.security2.JwtTokenProvider;
import com.example.MyFirstProject.service.EmailService;
import com.example.MyFirstProject.service.UserService;
import com.example.MyFirstProject.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.AbstractMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static com.example.MyFirstProject.security2.SecurityConstants.HEADER_STRING;
import static com.example.MyFirstProject.security2.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MessageSource messages;

    @Autowired
    private Environment env;

//    @ExceptionHandler({HttpClientErrorException.class})
//    public String handleException() {
//        return "Error";
//    }

    @PostMapping("/login")
    public ResponseEntity<Map.Entry<String, String>> signIn(@RequestBody @Validated final SingInDTO singInDTO) {

        String token = userService.signIn(singInDTO);

        MultiValueMap<String, String> headers = new HttpHeaders();

        headers.add(HEADER_STRING, TOKEN_PREFIX + token);

        Map.Entry<String, String> tok = new AbstractMap.SimpleEntry<>("token", token);

        return new ResponseEntity<>(tok, headers, HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<String> singUpUser(@RequestBody @Validated final SingUpDTO singUpDTO, final HttpServletRequest request){

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
    public ResponseEntity<Map.Entry<String, String>> processConfirmationForm(@RequestParam("token") final String token){

        User user = userService.findOneByUsernameOrEmail(jwtTokenProvider.getUsername(token));

        user.setEnabled(true);

        userService.saveAndFlush(user);

        MultiValueMap<String, String> headers = new HttpHeaders();

        headers.add(HEADER_STRING, TOKEN_PREFIX + token);

        Map.Entry<String, String> tok = new AbstractMap.SimpleEntry<>("token", token);

        return new ResponseEntity<>(tok, headers, HttpStatus.OK);
    }

    @PatchMapping("/myself")
    public String updateAccount(@RequestBody @Validated final UserUpdateDTO userUpdateDTO, final Authentication authentication) {

        User user = userService.findOneByUsernameOrEmail(authentication.getName());

        userService.updateAccount(user, userUpdateDTO);

        return "successfully";
    }

    @GetMapping("/me")
    public User whoAmI(final Authentication authentication) {

        return userService.findOneByUsernameOrEmail(authentication.getName());

    }
// переробити на гет запит  і окремо пост зробити

    @PostMapping("/resetPassword")
    public GenericResponse resetPassword(final HttpServletRequest request, @RequestParam("email") final String userEmail) {
        final User user = userService.findUserByEmail(userEmail);
        if (user != null) {
            final String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            emailService.sendEmail(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, user));
        }
        return new GenericResponse(messages.getMessage("message.resetPasswordEmail", null, request.getLocale()));
    }

    @GetMapping("/changePassword")
    public String showChangePasswordPage(Locale locale, Model model,
                                         @RequestParam("id") long id, @RequestParam("token") String token) {
        String result = securityService.validatePasswordResetToken(id, token);
        if (result != null) {
            model.addAttribute("message",
                    messages.getMessage("auth.message." + result, null, locale));
            return "redirect:/login?lang=" + locale.getLanguage();
        }
        return "redirect:/updatePassword.html?lang=" + locale.getLanguage();
    }

    @PostMapping("/savePassword")
    public GenericResponse savePassword(Locale locale,
                                        @RequestBody @Validated PasswordDTO passwordDto, @RequestParam("id") Long id, @RequestParam("token") String token) {

        User user = userService.validatePasswordResetToken(id, token);

        userService.changeUserPassword(user, passwordDto.getNewPassword());
        return new GenericResponse(
                messages.getMessage("message.resetPasswordSuc", null, locale));
    }

    // ============== NON-API ============

    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale, final String token, final User user) {
        final String url = contextPath + "/users/changePassword?id=" + user.getId() + "&token=" + token;
        final String message = messages.getMessage("message.resetPassword", null, locale);
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body, User user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    private String getAppUrl(HttpServletRequest request) {
        String subject = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        System.out.println(subject);
        System.out.println(request.getServerName() + "============" + request.getServerPort() + "===========" + request.getContextPath());
        return subject;
    }

}
