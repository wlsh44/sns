package com.example.sns.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class AuthConfig {

    private final AuthProperties properties;

    @Bean
    public WebClient oauthProvider() {
        return WebClient.create(properties.getTokenUrl());
    }
}
