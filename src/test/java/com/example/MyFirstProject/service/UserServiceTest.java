package com.example.MyFirstProject.service;

import com.example.MyFirstProject.exception.CustomException;
import com.example.MyFirstProject.model.Role;
import com.example.MyFirstProject.model.User;
import com.example.MyFirstProject.model.dto.SingUpDTO;
import com.example.MyFirstProject.repository.UserRepository;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.exceptions.misusing.WrongTypeOfReturnValue;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    public void signUpUser() {
        SingUpDTO singUpDTO = new SingUpDTO();

        singUpDTO.setUsername("ivan");
        singUpDTO.setEmail("ivan@gmail.com");
        singUpDTO.setPassword("password");

        User user = userService.signUpUser(singUpDTO);

        Assert.assertEquals(singUpDTO.getUsername(), user.getUsername());
        Assert.assertFalse(user.isEnabled());
        Assert.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));

        Mockito.verify(userRepository, Mockito.times(1)).save(user);

    }

    @Test(expected = WrongTypeOfReturnValue.class)
    public void singUpUserFailTest() {
        SingUpDTO singUpDTO = new SingUpDTO();

        singUpDTO.setUsername("ivan");
        singUpDTO.setEmail("ivan@gmail.com");
        singUpDTO.setPassword("password");

        Mockito.doReturn(new User())
                .when(userRepository)
                .existsByUsernameOrEmail(singUpDTO.getUsername(), singUpDTO.getEmail());

        userService.signUpUser(singUpDTO);
    }

    @Test
    public void findByUsername() {
        String username = "ivan";

        Mockito.when(userRepository.findByUsername(username)).thenReturn(new User());

        userService.findByUsername(username);
    }

    @Test(expected = CustomException.class)
    public void findByUsernameFailTest() {
        String username = "ivan";

        Mockito.when(userRepository.findByUsername(username)).thenReturn(null);

        userService.findByUsername(username);
    }
}