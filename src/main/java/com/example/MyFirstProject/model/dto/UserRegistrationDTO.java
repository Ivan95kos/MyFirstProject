package com.example.MyFirstProject.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class UserRegistrationDTO {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 1)
    private String password;
}
