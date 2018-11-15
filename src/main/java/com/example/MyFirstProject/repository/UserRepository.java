package com.example.MyFirstProject.repository;

import com.example.MyFirstProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    User findOneById(Long idUser);

    User findByEmail(String email);

    User findByUsername(String username);

    User findOneByUsernameOrEmail(String username, String Email);

    boolean existsByUsernameOrEmail(String username, String Email);
}
