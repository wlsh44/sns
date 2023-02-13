package com.example.sns.auth.infrastructure;

import com.example.sns.auth.config.AuthProperties;
import com.example.sns.auth.presentation.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class GoogleClientTest {

    GoogleClient client;

    AuthProperties properties;

    WebClient webClient;

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
        webClient = mock(WebClient.class, Answers.RETURNS_DEEP_STUBS);
        client = new GoogleClient(properties, webClient);
    }

    @Test
    @DisplayName("올바른 redirect URI 리턴해야 함")
    void redirectURI() throws Exception {
        //given
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

    @Test
    @DisplayName("id token 리턴해야 함")
    void idToken() throws Exception {
        //given
        String authenticationCode = "code";
        String expect = "idToken";
        TokenResponse response = mock(TokenResponse.class);
        given(webClient.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(any())
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block())
                .willReturn(response);
        given(response.getIdToken())
                .willReturn(expect);

        //when
        String idToken = client.getIdToken(authenticationCode);

        //then
        assertThat(idToken).isEqualTo(expect);
    }
}