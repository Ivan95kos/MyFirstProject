package com.example.MyFirstProject.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String nameRole;

    @ManyToMany(mappedBy = "authorities")
    private Set<User> users = new HashSet<>();

    @Override
    public String getAuthority() {
        return null;
    }
}
