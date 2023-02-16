package com.example.sns.auth.infrastructure;

import com.example.sns.auth.application.dto.OAuthUserInfoDto;
import com.example.sns.auth.config.AuthProperties;
import com.example.sns.auth.infrastructure.dto.GoogleAuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
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
    String grantType;
    List<String> scopes;

    @BeforeEach
    void init() {
        authURI = "https://accounts.google.com/o/oauth2/v2/auth";
        redirectURI = "http://localhost:8080/google/sign-in";
        clientId = "client-id";
        clientSecret = "client-secret";
        tokenURI = "https://oauth2.googleapis.com/token";
        grantType = "authorization_code";
        scopes = List.of("openid", "profile", "email");
        properties = new AuthProperties(redirectURI, authURI, clientId, clientSecret, tokenURI, grantType, scopes);
        webClient = mock(WebClient.class, Answers.RETURNS_DEEP_STUBS);
        client = new GoogleClient(properties, webClient, new ObjectMapper());
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
        GoogleAuthResponse response = mock(GoogleAuthResponse.class);
        given(webClient.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(any())
                .retrieve()
                .bodyToMono(GoogleAuthResponse.class)
                .block())
                .willReturn(response);
        given(response.getIdToken())
                .willReturn(expect);

        //when
        String idToken = client.getIdToken(authenticationCode);

        //then
        assertThat(idToken).isEqualTo(expect);
    }

    @Test
    @DisplayName("id token에서 유저 정보 가져와야 함")
    void getUserInfoDto() throws Exception {
        //given
        String idToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6Iuq5gOynhO2YuCIsImVtYWlsIjoidGVzdEB0ZXN0LnRlc3QiLCJwaWN0dXJlIjoiaHR0cDovL2ltYWdldXJsLnRlc3QiLCJpYXQiOjE1MTYyMzkwMjJ9.UNDTgoj1GKEy0XrY_RkfyHUtxJE6ha4yd84IED_xC6c";
        String socialId = "1234567890";
        String name = "김진호";
        String email = "test@test.test";

        //when
        OAuthUserInfoDto userInfo = client.getUserInfo(idToken);

        //then
        assertSoftly(softAssertions -> {
                softAssertions.assertThat(userInfo.getSocialId()).isEqualTo(socialId);
                softAssertions.assertThat(userInfo.getName()).isEqualTo(name);
                softAssertions.assertThat(userInfo.getEmail()).isEqualTo(email);
            }
        );
    }
}