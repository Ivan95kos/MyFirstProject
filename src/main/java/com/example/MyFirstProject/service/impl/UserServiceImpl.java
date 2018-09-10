package com.example.MyFirstProject.service.impl;

import com.example.MyFirstProject.model.User;
import com.example.MyFirstProject.repository.UserRepository;
import com.example.MyFirstProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sun.security.util.Password;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registrationUser(User user) {

        return userRepo.save(new User(user.getEmail(), passwordEncoder.encode(user.getPassword())));
    }

    @Override
    public void delete(User user) {
        userRepo.delete(user);
    }

    @Override
    public User getByName(String name) {
        return userRepo.findByFirstName(name);
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
