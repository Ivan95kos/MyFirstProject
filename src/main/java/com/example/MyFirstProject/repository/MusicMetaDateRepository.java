package com.example.MyFirstProject.repository;

import com.example.MyFirstProject.model.MusicMetaDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicMetaDateRepository extends JpaRepository<MusicMetaDate, Long> {
}
