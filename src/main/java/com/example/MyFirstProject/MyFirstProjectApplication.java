package com.example.MyFirstProject;

import com.example.MyFirstProject.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//@SpringBootApplication(exclude = SecurityAutoConfiguration.class)

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class MyFirstProjectApplication {

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(8);
//    }

//    @Bean
//    public AuthenticationManager authenticationManagerBean(){
//       return new AuthenticationManager() {
//            @Override
//            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//                return null;
//            }
//        };
//    }

    public static void main(String[] args) {
        SpringApplication.run(MyFirstProjectApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(8);
    }
}
