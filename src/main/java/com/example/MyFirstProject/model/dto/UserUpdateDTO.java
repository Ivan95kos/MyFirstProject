package com.example.MyFirstProject.model.dto;

import com.example.MyFirstProject.model.Language;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class UserUpdateDTO {

    @NotNull
    @Size(min = 1, max = 40)
    private String firstName;

    @NotNull
    @Size(min = 1, max = 40)
    private String lastName;

    @NotNull
    @Email
    private String email;

    @NotNull
    private int age;

    private Set<Language> languages = new HashSet<>();

}