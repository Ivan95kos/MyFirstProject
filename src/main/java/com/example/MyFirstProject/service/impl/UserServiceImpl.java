package com.example.MyFirstProject.service.impl;

import com.example.MyFirstProject.model.Language;
import com.example.MyFirstProject.model.User;
import com.example.MyFirstProject.model.dto.UserRegistrationDTO;
import com.example.MyFirstProject.model.dto.UserUpdateDTO;
import com.example.MyFirstProject.repository.LanguageRepository;
import com.example.MyFirstProject.repository.UserRepository;
import com.example.MyFirstProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registrationUser(UserRegistrationDTO userRegistrationDTO) {

        return userRepo.save(new User(userRegistrationDTO.getEmail(), passwordEncoder.encode(userRegistrationDTO.getPassword())));
    }

    @Override
    public User updateUser(String email, UserUpdateDTO userUpdateDTO) {
        User updateUser = userRepo.findByEmail(email);

        Set<Language> language = userUpdateDTO.getLanguages();

        languageRepository.saveAll(language);

//        language.forEach(lan -> lan.getUsers().add(updateUser));

        updateUser.setFirstName(userUpdateDTO.getFirstName());
        updateUser.setLastName(userUpdateDTO.getLastName());
        updateUser.setAge(userUpdateDTO.getAge());
        updateUser.setLanguages(userUpdateDTO.getLanguages());

        return userRepo.save(updateUser);
    }

    @Override
    public void delete(User user) {
        userRepo.delete(user);
    }

    @Override
    public User getByName(String name) {
        return userRepo.findByEmail(name);
    }

    @Override
    public User editUser(User user) {
        return userRepo.saveAndFlush(user);
    }

    @Override
    public List<User> getAll() {
        return userRepo.findAll();
    }

}
