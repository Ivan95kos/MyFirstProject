package com.example.MyFirstProject.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String header;

    @Column
    private String title;

    @Column
    private String artist;

    @Column
    private String albumm;

    @Column
    private int year;

    @Column
    private String commemnt;

    @Column
    private int track;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Music> music;

}
