package com.example.sns.auth.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "oauth.google")
public class AuthProperties {

    private final String redirectUri;
    private final String authUri;
    private final String clientId;
    private final String clientSecret;
    private final String tokenUrl;
    private final String grantType;
    private final List<String> scopes;
}
