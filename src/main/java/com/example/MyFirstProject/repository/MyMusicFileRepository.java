package com.example.MyFirstProject.repository;

import com.example.MyFirstProject.model.MyMusicFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyMusicFileRepository extends JpaRepository<MyMusicFile, Long> {

    List<MyMusicFile> findByUserId(Long userId);

}
