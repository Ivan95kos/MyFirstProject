package com.example.MyFirstProject.service.impl;

import com.example.MyFirstProject.model.User;
import com.example.MyFirstProject.model.dto.UserDTO;
import com.example.MyFirstProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    //    @Autowired
//    private LanguageRepository languageRepository;
//
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User signUp(UserDTO userDTO) {

        return userRepository.save(new User(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword())));
    }

//    @Override
//    public User updateUser(String username, UserUpdateDTO userUpdateDTO) {
//
//        User updateUser = userRepository.findOneByUsername(username);
//
//        Set<Language> language = userUpdateDTO.getLanguages();
//
//        languageRepository.saveAll(language);
//
////        language.forEach(lan -> lan.getUsers().add(updateUser));
//
//        updateUser.setFirstName(userUpdateDTO.getFirstName());
//        updateUser.setLastName(userUpdateDTO.getLastName());
//        updateUser.setAge(userUpdateDTO.getAge());
//        updateUser.setLanguages(userUpdateDTO.getLanguages());
//
//        return userRepository.save(updateUser);
//    }

//    @Override
//    public User findByUsername(String username) {
//
//        return userRepository.findOneByUsername(username);
//    }
//
//    @Override
//    public void delete(User user) {
//        userRepository.delete(user);
//    }
//
//    @Override
//    public User editUser(User user) {
//        return userRepository.saveAndFlush(user);
//    }
//
//    @Override
//    public List<User> getAll() {
//        return userRepository.findAll();
//    }

    // Let people login with either username or email
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findOneByUsername(username);
        System.out.println(user);
        if (user == null) {
            throw new UsernameNotFoundException("User with name: " + username + " not found");
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true, true, true, true,
                new ArrayList<>());
    }
}
