package com.example.MyFirstProject.service;

import com.example.MyFirstProject.repository.PasswordResetTokenRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

@Service
@Transactional
@NoArgsConstructor
public class TokensPurgeTask {

//    @Autowired
//    VerificationTokenRepository tokenRepository;

    @Autowired
    PasswordResetTokenRepository passwordTokenRepository;

    @Scheduled(cron = "${purge.cron.expression}")
    public void purgeExpired() {

        Date now = Date.from(Instant.now());

        passwordTokenRepository.deleteByExpiryDateLessThan(now);
//        tokenRepository.deleteAllExpiredSince(now);
    }
}
