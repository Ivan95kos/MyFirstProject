package com.example.MyFirstProject.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@ToString
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private int age;

    @Column
    private String password;

    @Column
    private String email;

//    private Collection<Role> authorities;
//
//    private String userName;
//
//    private boolean accountNonExpired;
//
//    private boolean accountNonLocked;
//
//    private boolean credentialsNonExpired;
//
//    private boolean enabled;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "lang_x_user",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "lang_id")}
    )
    private Set<Language> languages = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<File> files;

    @ManyToMany
    @JoinTable(
            name = "user_x_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<Role> roles = new HashSet<>();

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
