package com.example.MyFirstProject.service;

import com.example.MyFirstProject.exception.CustomException;
import com.example.MyFirstProject.model.Language;
import com.example.MyFirstProject.model.PasswordResetToken;
import com.example.MyFirstProject.model.Role;
import com.example.MyFirstProject.model.User;
import com.example.MyFirstProject.model.dto.SingInDTO;
import com.example.MyFirstProject.model.dto.SingUpDTO;
import com.example.MyFirstProject.model.dto.UserUpdateDTO;
import com.example.MyFirstProject.repository.LanguageRepository;
import com.example.MyFirstProject.repository.MyFileRepository;
import com.example.MyFirstProject.repository.PasswordResetTokenRepository;
import com.example.MyFirstProject.repository.UserRepository;
import com.example.MyFirstProject.security2.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private MyFileRepository myFileRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

//    @Autowired
//    private RoleService roleService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String signIn(final SingInDTO singInDTO) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(singInDTO.getUsernameOrEmail(), singInDTO.getPassword()));

            User user = userRepository.findOneByUsernameOrEmail(singInDTO.getUsernameOrEmail(), singInDTO.getUsernameOrEmail());

            return jwtTokenProvider.createToken(user);
        } catch (AccountStatusException e) {
            throw new CustomException("Account not activated. Verify email", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public String signUpUser(final SingUpDTO singUpDTO) {

        if (!userRepository.existsByUsernameOrEmail(singUpDTO.getUsername(), singUpDTO.getEmail())) {
            User user = new User(singUpDTO.getUsername(), singUpDTO.getEmail(), passwordEncoder.encode(singUpDTO.getPassword()));

            user.setEnabled(false);

            user.setRoles(Collections.singleton(Role.USER));

            userRepository.save(user);

            return jwtTokenProvider.createToken(user);
        } else {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public String signUpAdmin(final SingUpDTO singUpDTO) {

        if (!userRepository.existsByUsernameOrEmail(singUpDTO.getUsername(), singUpDTO.getEmail())) {
            User user = new User(singUpDTO.getUsername(), singUpDTO.getEmail(), passwordEncoder.encode(singUpDTO.getPassword()));

            user.setEnabled(false);

            user.setRoles(Collections.singleton(Role.ADMIN));

            userRepository.save(user);

            return jwtTokenProvider.createToken(user);
        } else {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public User updateAccount(final User user, final UserUpdateDTO userUpdateDTO) {

        Set<Language> language = userUpdateDTO.getLanguages();

        languageRepository.saveAll(language);

//        language.forEach(lan -> lan.getUsers().add(user));

        user.setFirstName(userUpdateDTO.getFirstName());
        user.setLastName(userUpdateDTO.getLastName());
        user.setEmail(userUpdateDTO.getEmail());
        user.setAge(userUpdateDTO.getAge());
        user.setLanguages(userUpdateDTO.getLanguages());

        return userRepository.saveAndFlush(user);
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

    public void deleteId(User user) {
        userRepository.deleteById(user.getId());
    }

    public User saveAndFlush(User user) {
        return userRepository.saveAndFlush(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User findUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail);
    }

    public void createPasswordResetTokenForUser(final User user,final String token) {
        final PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    public void changeUserPassword(final User user, final String password){
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public User validatePasswordResetToken(long id, String token) {
        PasswordResetToken passToken =
                passwordResetTokenRepository.findByToken(token);
        if ((passToken == null) || (passToken.getUser()
                .getId() != id)) {
            throw new CustomException("invalidToken", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate()
                .getTime() - cal.getTime()
                .getTime()) <= 0) {
            throw new CustomException("invalidToken", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return passToken.getUser();

    }
}
