package com.example.MyFirstProject.service;

import com.example.MyFirstProject.exception.CustomException;
import com.example.MyFirstProject.model.Language;
import com.example.MyFirstProject.model.Role;
import com.example.MyFirstProject.model.User;
import com.example.MyFirstProject.model.dto.UserDTO;
import com.example.MyFirstProject.model.dto.UserUpdateDTO;
import com.example.MyFirstProject.repository.LanguageRepository;
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

//import com.example.MyFirstProject.model.Role;
//import com.example.MyFirstProject.repository.RoleRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LanguageRepository languageRepository;

//    @Autowired
//    private RoleService roleService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String signIn(UserDTO userDTO) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));
            User user = userRepository.findOneByUsername(userDTO.getUsername());
            return jwtTokenProvider.createToken(user);
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public String signUp(UserDTO userDTO) {

        if (!userRepository.existsByUsername(userDTO.getUsername())) {
            User user = new User(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()));

            user.setRoles(Collections.singleton(Role.USER));

            userRepository.save(user);

            return jwtTokenProvider.createToken(user);
        } else {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public User updateUser(User updateUser, UserUpdateDTO userUpdateDTO) {

        Set<Language> language = userUpdateDTO.getLanguages();

        languageRepository.saveAll(language);

//        language.forEach(lan -> lan.getUsers().add(updateUser));

        updateUser.setFirstName(userUpdateDTO.getFirstName());
        updateUser.setLastName(userUpdateDTO.getLastName());
        updateUser.setEmail(userUpdateDTO.getEmail());
        updateUser.setAge(userUpdateDTO.getAge());
        updateUser.setLanguages(userUpdateDTO.getLanguages());

        return userRepository.save(updateUser);
    }

    public User findOneByUsername(String username) {

        User user = userRepository.findOneByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User with name: " + username + " not found");
        }

        return user;
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public User editUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }


}
