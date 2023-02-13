package com.example.sns.auth.infrastructure;

import com.example.sns.auth.config.AuthProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GoogleClientTest {

    GoogleClient client;

    AuthProperties properties;

    String clientId;
    String clientSecret;
    String redirectURI;
    String authURI;
    String tokenURI;
    List<String> scopes;

    @BeforeEach
    void init() {
        authURI = "https://accounts.google.com/o/oauth2/v2/auth";
        redirectURI = "http://localhost:8080/google/sign-in";
        clientId = "client-id";
        clientSecret = "client-secret";
        tokenURI = "https://oauth2.googleapis.com/token";
        scopes = List.of("openid", "profile", "email");
        properties = new AuthProperties(redirectURI, authURI, clientId, clientSecret, tokenURI, scopes);
    }

    @Test
    @DisplayName("올바른 redirect URI 리턴해야 함")
    void redirectURI() throws Exception {
        //given
        client = new GoogleClient(properties);
        URI expect = UriComponentsBuilder.fromUriString(authURI)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectURI)
                .queryParam("response_type", "code")
                .queryParam("scope", String.join(" ", scopes))
                .build().toUri();

        //when
        URI result = client.getAuthRedirectURI();

        //then
        assertThat(result).isEqualTo(expect);
    }
}