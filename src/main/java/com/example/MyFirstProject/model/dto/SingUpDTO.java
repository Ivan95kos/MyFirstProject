package com.example.MyFirstProject.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class SingUpDTO {

    @NotNull
    @Size(min = 1, max = 40)
    private String username;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 1, max = 40)
    private String password;

}
