package com.example.MyFirstProject.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@ToString
public class UserDTO {

    @NotNull
    @Size(min = 1, max = 40)
    private String username;

    @NotNull
    @Size(min = 1, max = 40)
    private String password;
}
