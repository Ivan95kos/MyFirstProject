package com.example.MyFirstProject.domain;

import com.example.MyFirstProject.Validation.PasswordMatches;
import com.example.MyFirstProject.Validation.ValidEmail;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Size;

@PasswordMatches
public class User {

    @NonNull
    @Size(min = 1)
    private String firstName;

    @NonNull
    @Size(min = 1)
    private String lastName;

    private int age;

    @NonNull
    private String password;

    @NonNull
    @Size(min = 1)
    private String matchingPassword;

    @NonNull
    @ValidEmail
    private String email;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", password='" + password + '\'' +
                ", matchingPassword='" + matchingPassword + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
