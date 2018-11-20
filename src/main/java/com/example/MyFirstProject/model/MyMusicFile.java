package com.example.MyFirstProject.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mpatric.mp3agic.Mp3File;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Entity
public class MyMusicFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String fileName;

    @Column
    private String localAddress;

    @Column
    private String contentType;

    @Transient
    private Mp3File mp3File;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "music_meta_date_id")
    private MusicMetaDate musicMetaDate;
}
