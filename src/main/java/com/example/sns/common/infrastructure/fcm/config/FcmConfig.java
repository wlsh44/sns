package com.example.sns.common.infrastructure.fcm.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Configuration
public class FcmConfig {

    private final String fcmPrivateKeyPath;
    private final String firebaseScope;

    public FcmConfig(@Value("${fcm.key.path}") String fcmPrivateKeyPath,
                     @Value("${fcm.key.scope}") String firebaseScope) {
        this.fcmPrivateKeyPath = fcmPrivateKeyPath;
        this.firebaseScope = firebaseScope;
    }

    @Bean
    public FirebaseMessaging firebaseApp() {
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(createCredentials())
                .build();
        FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);
        log.info("Firebase가 연결되었습니다.");
        return FirebaseMessaging.getInstance(firebaseApp);
    }

    private GoogleCredentials createCredentials() {
        try (InputStream inputStream = new ClassPathResource(fcmPrivateKeyPath).getInputStream()) {
            return GoogleCredentials
                    .fromStream(inputStream)
                    .createScoped(List.of(firebaseScope));
        } catch (IOException e) {
            log.error("Firebase 인증에 실패했습니다 e = {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
