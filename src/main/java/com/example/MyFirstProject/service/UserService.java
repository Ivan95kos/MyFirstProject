package com.example.MyFirstProject.service;

import com.example.MyFirstProject.model.User;
import com.example.MyFirstProject.model.dto.UserRegistrationDTO;
import com.example.MyFirstProject.model.dto.UserUpdateDTO;

import java.util.List;

public interface UserService  {

    User registrationUser(UserRegistrationDTO userRegistrationDTO);

    User updateUser(String email, UserUpdateDTO userUpdateDTO);

    void delete(User user);

    User getByName(String name);

    User editUser(User user);

    List<User> getAll();

}
