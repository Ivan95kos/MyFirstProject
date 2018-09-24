package com.example.MyFirstProject.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserDTO {

    @NotNull
    @Size(min = 1, max = 40)
    private String username;

    @NotNull
    @Size(min = 1, max = 40)
    private String password;


}
