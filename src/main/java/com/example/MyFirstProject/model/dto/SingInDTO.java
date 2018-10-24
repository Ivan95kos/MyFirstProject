package com.example.MyFirstProject.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class SingInDTO {

    @NotEmpty
    private String usernameOrEmail;

    @NotEmpty
    private String password;
}
