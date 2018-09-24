package com.example.MyFirstProject.repository;

import com.example.MyFirstProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findOneByUsername(String username);

    boolean existsByUsername(String username);
}
