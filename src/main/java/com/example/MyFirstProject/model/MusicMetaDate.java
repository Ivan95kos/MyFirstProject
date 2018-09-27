package com.example.MyFirstProject.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class MusicMetaDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_music")
    private String nameMusic;

    @Column
    private String title;

    @Column
    private String artist;

    @Column
    private String album;

    @Column
    private String year;

    @Column
    private String comment;

    @Column
    private String track;

    @Column
    private String genre;

//    @ManyToOne
//    @JoinColumn(name = "album_id")
//    private Album album;
}
