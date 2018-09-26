package com.example.MyFirstProject.repository;

import com.example.MyFirstProject.model.MyFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<MyFile, String> {
}
