package com.example.sns.auth.presentation;

import com.example.sns.auth.application.AuthService;
import com.example.sns.auth.exception.OAuthException;
import com.example.sns.auth.presentation.dto.TokenResponse;
import com.example.sns.common.exception.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.StatusResultMatchersExtensionsKt.isEqualTo;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthService authService;

    @Autowired
    ObjectMapper mapper;

    @Test
    @DisplayName("OAuth 인증 화면으로 리다이렉트 해야 함")
    void redirectToProvider() throws Exception {
        //given
        String expectUri = "http://localhost:8080";
        URI uri = UriComponentsBuilder.fromUriString(expectUri).build().toUri();
        given(authService.getAuthRedirectURI()).willReturn(uri);

        //when then
        mockMvc.perform(get("/auth/redirect"))
                .andExpect(status().isSeeOther())
                .andExpect(header().stringValues(LOCATION, expectUri));
    }

    @Test
    @DisplayName("로그인 성공하면 토큰을 응답해야 함")
    void signIn() throws Exception {
        //given
        TokenResponse tokenResponse = new TokenResponse("jwt token");
        given(authService.signIn(any())).willReturn(tokenResponse);

        //when then
        mockMvc.perform(get("/auth/sign-in")
                        .param("code", "code"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(tokenResponse)));
    }

    @Test
    @DisplayName("인증 코드가 잘못된 경우 예외를 반환해야 함")
    void signIn_OAuthException() throws Exception {
        //given
        ErrorResponse errorResponse = new ErrorResponse(OAuthException.ERROR_MSG);
        given(authService.signIn(any())).willThrow(new OAuthException());

        //when then
        mockMvc.perform(get("/auth/sign-in")
                        .param("code", "wrong code"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(mapper.writeValueAsString(errorResponse)));
    }
}