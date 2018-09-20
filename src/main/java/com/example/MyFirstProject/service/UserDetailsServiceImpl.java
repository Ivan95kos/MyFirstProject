package com.example.MyFirstProject.service;

import com.example.MyFirstProject.model.User;
import com.example.MyFirstProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findOneByUsername(username);
//        Hibernate.initialize(user.getRoles());
//        System.out.println(user.getRoles());
        if (user == null) {
            throw new UsernameNotFoundException("User with name: " + username + " not found");
        }

//        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true, true, true, true,
//                new ArrayList<>());
        return org.springframework.security.core.userdetails.User//
                .withUsername(username)//
                .password(user.getPassword())//
                .authorities(user.getRoles())//
                .accountExpired(false)//
                .accountLocked(false)//
                .credentialsExpired(false)//
                .disabled(false)//
                .build();
    }
}
