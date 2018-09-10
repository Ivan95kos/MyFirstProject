package com.example.MyFirstProject.service;

import com.example.MyFirstProject.model.User;

import java.util.List;

public interface UserService {

    User registrationUser(User user);

    void delete(User user);

    User getByName(String name);

    User editUser(User user);

    List<User> getAll();

}
