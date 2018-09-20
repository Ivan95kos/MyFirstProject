package com.example.MyFirstProject.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "language_of_communication")
    private String languageOfCommunication;

    @ManyToMany(mappedBy = "languages")
    private Set<User> users = new HashSet<>();

}
