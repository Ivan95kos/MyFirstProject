package com.example.MyFirstProject.service;

//Планування завдань
//@Service
//@Transactional
//@NoArgsConstructor
//public class TokensPurgeTask {
//
////    @Autowired
////    VerificationTokenRepository tokenRepository;
//
//    @Autowired
//    PasswordResetTokenRepository passwordTokenRepository;
//
//    @Scheduled(cron = "${purge.cron.expression}")
//    public void purgeExpired() {
//
//        Date now = Date.from(Instant.now());
//
//        passwordTokenRepository.deleteByExpiryDateLessThan(now);
////        tokenRepository.deleteAllExpiredSince(now);
//    }
//}
