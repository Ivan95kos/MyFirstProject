package com.example.MyFirstProject.repository;

import com.example.MyFirstProject.model.MyFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyFileRepository extends JpaRepository<MyFile, Long> {

    List<MyFile> findByUserId(Long userId);

}
