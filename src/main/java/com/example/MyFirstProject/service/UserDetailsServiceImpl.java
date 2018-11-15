package com.example.MyFirstProject.service;

import com.example.MyFirstProject.model.User;
import com.example.MyFirstProject.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findOneByUsernameOrEmail(usernameOrEmail, usernameOrEmail);

//        Hibernate.initialize(user.getUsername());
//        System.out.println(user.getUsername());

        if (user == null) {
            log.info("User with name: " + usernameOrEmail + " not found");
            throw new UsernameNotFoundException("User with name: " + usernameOrEmail + " not found");
        }

//        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true, true, true, true,
//                new ArrayList<>());
        return org.springframework.security.core.userdetails.User//
                .withUsername(user.getUsername())//
                .password(user.getPassword())//
                .authorities(user.getRoles())//
                .accountExpired(false)//
                .accountLocked(false)//
                .credentialsExpired(false)//
                .disabled(!user.isEnabled())//
                .build();
    }
}
