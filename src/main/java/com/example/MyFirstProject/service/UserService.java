package com.example.MyFirstProject.service;

import com.example.MyFirstProject.exception.CustomException;
import com.example.MyFirstProject.model.Language;
import com.example.MyFirstProject.model.Role;
import com.example.MyFirstProject.model.User;
import com.example.MyFirstProject.model.dto.UserDTO;
import com.example.MyFirstProject.model.dto.UserUpdateDTO;
import com.example.MyFirstProject.repository.LanguageRepository;
import com.example.MyFirstProject.repository.MyFileRepository;
import com.example.MyFirstProject.repository.UserRepository;
import com.example.MyFirstProject.security2.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

//    @Autowired
//    private RoleService roleService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String signIn(final UserDTO userDTO) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));
            User user = userRepository.findOneByUsername(userDTO.getUsername());
            return jwtTokenProvider.createToken(user);
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public String signUpUser(final UserDTO userDTO) {

        if (!userRepository.existsByUsername(userDTO.getUsername())) {
            User user = new User(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()));

            user.setRoles(Collections.singleton(Role.USER));

            userRepository.save(user);

            return jwtTokenProvider.createToken(user);
        } else {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public String signUpAdmin(final UserDTO userDTO) {

        if (!userRepository.existsByUsername(userDTO.getUsername())) {
            User user = new User(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()));

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

    public User findOneByUsername(final String username) {

        User user = userRepository.findOneByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with name: " + username + " not found");
        }

        return user;
    }

    public void delete(final User user) {
        userRepository.delete(user);
    }

    public void deleteId(User user) {
        userRepository.deleteById(user.getId());
    }

    public User editUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

}
