package com.example.MyFirstProject.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class PasswordDTO {

    @NotNull
    @Size(min = 5)
    private String newPassword;
}
