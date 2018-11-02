package com.example.MyFirstProject.repository;

import com.example.MyFirstProject.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);

    void deleteByExpiryDateLessThan(Date now);
}
