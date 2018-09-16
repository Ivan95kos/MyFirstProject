package com.example.MyFirstProject.service;

import com.example.MyFirstProject.model.User;
import com.example.MyFirstProject.model.dto.UserDTO;
import com.example.MyFirstProject.model.dto.UserUpdateDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService{

    User signUp(UserDTO userDTO);

    User updateUser(String email, UserUpdateDTO userUpdateDTO);

    User findByUsername(String username);

    void delete(User user);

    User editUser(User user);

    List<User> getAll();

}
