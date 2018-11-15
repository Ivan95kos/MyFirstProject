package com.example.MyFirstProject.service;

import com.example.MyFirstProject.exception.CustomException;
import com.example.MyFirstProject.model.Language;
import com.example.MyFirstProject.model.Role;
import com.example.MyFirstProject.model.User;
import com.example.MyFirstProject.model.dto.SingInDTO;
import com.example.MyFirstProject.model.dto.SingUpDTO;
import com.example.MyFirstProject.model.dto.UserUpdateDTO;
import com.example.MyFirstProject.repository.LanguageRepository;
import com.example.MyFirstProject.repository.UserRepository;
import com.example.MyFirstProject.security2.JwtTokenProvider;
import com.example.MyFirstProject.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import static com.example.MyFirstProject.security2.SecurityConstants.HEADER_STRING;
import static com.example.MyFirstProject.security2.SecurityConstants.TOKEN_PREFIX;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MessageSource messages;

    @Autowired
    private Environment env;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    public User signIn(final SingInDTO singInDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(singInDTO.getUsernameOrEmail(), singInDTO.getPassword()));

            return findOneByUsernameOrEmail(singInDTO.getUsernameOrEmail());

        } catch (AccountStatusException e) {
            throw new CustomException("message.AccountNotActivated", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (AuthenticationException e) {
            throw new CustomException("message.InvalidUsernameOrPassword", HttpStatus.NOT_FOUND);
        }
    }

    public User signUpUser(final SingUpDTO singUpDTO) {
        if (!userRepository.existsByUsernameOrEmail(singUpDTO.getUsername(), singUpDTO.getEmail())) {
            User user = new User(
                    singUpDTO.getUsername(),
                    singUpDTO.getEmail(),
                    passwordEncoder.encode(singUpDTO.getPassword()));

            user.setEnabled(false);

            user.setRoles(Collections.singleton(Role.USER));

            return userRepository.save(user);

        } else {
            throw new CustomException("UsernameOrEmail is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public User signUpAdmin(final SingUpDTO singUpDTO) {
        if (!userRepository.existsByUsernameOrEmail(singUpDTO.getUsername(), singUpDTO.getEmail())) {
            User user = new User(
                    singUpDTO.getUsername(),
                    singUpDTO.getEmail(),
                    passwordEncoder.encode(singUpDTO.getPassword()));

            user.setEnabled(false);

            user.setRoles(Collections.singleton(Role.ADMIN));

            return userRepository.save(user);

        } else {
            throw new CustomException("UsernameOrEmail is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public User updateAccount(final String username, final UserUpdateDTO userUpdateDTO) {
        Set<Language> language = userUpdateDTO.getLanguages();

        languageRepository.saveAll(language);

        User user = findByUsername(username);

//        language.forEach(lan -> lan.getUsers().add(user));

        user.setFirstName(userUpdateDTO.getFirstName());
        user.setLastName(userUpdateDTO.getLastName());
        user.setAge(userUpdateDTO.getAge());
        user.setLanguages(userUpdateDTO.getLanguages());

        return userRepository.saveAndFlush(user);
    }

    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new CustomException("User with username: " + username + " not found", HttpStatus.NOT_FOUND);
        }
        return user;
    }

    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomException("User with email: " + email + " not found", HttpStatus.NOT_FOUND);
        }
        return user;
    }

    public User findOneByUsernameOrEmail(final String usernameOrEmail) {
        User user = userRepository.findOneByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        if (user == null) {
            throw new UsernameNotFoundException("User with nameOrEmail: " + usernameOrEmail + " not found");
        }

        return user;
    }

    public void delete(final User user) {
        userRepository.delete(user);
    }

    public void saveAndFlush(User user) {
        userRepository.saveAndFlush(user);
    }

    public void sendEmailActivation(final User user, final HttpServletRequest request) {

        String token = jwtTokenProvider.createToken(user);

//        String appUrl = request.getScheme() + "://" + request.getServerName();

        SimpleMailMessage registrationEmail = new SimpleMailMessage();
        registrationEmail.setTo(user.getEmail());
        registrationEmail.setSubject("Registration Confirmation");
        registrationEmail.setText("To confirm your e-mail address, please click the link below:\n"
                + getAppUrl(request) + "/users/activate?token=" + token);
        registrationEmail.setFrom(env.getProperty("support.email"));

        emailService.sendEmail(registrationEmail);
    }

    public ResponseEntity<GenericResponse> getResponseEntityWithToken(String token) {
        MultiValueMap<String, String> headers = new HttpHeaders();

        headers.add(HEADER_STRING, TOKEN_PREFIX + token);

        return new ResponseEntity<>(new GenericResponse(token, HttpStatus.OK), headers, HttpStatus.OK);
    }

    public User activationUser(String token) {
        jwtTokenProvider.validateToken(token);

        User user = findByUsername(jwtTokenProvider.getUsername(token));

        user.setEnabled(true);

        return userRepository.saveAndFlush(user);

    }

    public void resetPassword(String email, HttpServletRequest request) {
        final User user = findByEmail(email);

        user.setEnabled(false);

        final String token = jwtTokenProvider.createToken(user);

        emailService.sendEmail(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, user));

        userRepository.saveAndFlush(user);
    }

    public void savePassword(String username, String newPssword) {
        final User user = findByUsername(username);

        user.setPassword(passwordEncoder.encode(newPssword));

        userRepository.saveAndFlush(user);

    }

    // For reset Password

    private SimpleMailMessage constructResetTokenEmail(final String contextPath,
                                                       final Locale locale,
                                                       final String token,
                                                       final User user) {
        final String url = contextPath + "/users/changePassword?token=" + token;
        final String message = messages.getMessage("message.resetPassword", null, locale);
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(final String subject, final String body, final User user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    private String getAppUrl(final HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() +
                ":" + request.getServerPort() + request.getContextPath();
    }


    //Тимчасовий ключ безпеки при скиданні
//    public void createPasswordResetTokenForUser(final User user,final String token) {
//        final PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
//        passwordResetTokenRepository.save(passwordResetToken);
//    }

//    public User validatePasswordResetToken(long id, String token) {
//        PasswordResetToken passToken =
//                passwordResetTokenRepository.findByToken(token);
//        if ((passToken == null) || (passToken.getUser()
//                .getId() != id)) {
//            throw new CustomException("invalidToken", HttpStatus.UNPROCESSABLE_ENTITY);
//        }
//
//        Calendar cal = Calendar.getInstance();
//        if ((passToken.getExpiryDate()
//                .getTime() - cal.getTime()
//                .getTime()) <= 0) {
//            throw new CustomException("invalidToken", HttpStatus.UNPROCESSABLE_ENTITY);
//        }
//
//        return passToken.getUser();
//
//    }
}
