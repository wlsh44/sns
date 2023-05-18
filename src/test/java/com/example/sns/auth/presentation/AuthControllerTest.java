package com.example.sns.auth.presentation;

import com.example.sns.auth.exception.OAuthException;
import com.example.sns.auth.presentation.dto.TokenResponse;
import com.example.sns.common.support.MockControllerTest;
import com.example.sns.common.exception.dto.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends MockControllerTest {

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
        given(authService.signIn(any())).willThrow(new OAuthException());

        //when then
        mockMvc.perform(get("/auth/sign-in")
                        .param("code", "wrong code"))
                .andExpect(status().isBadRequest());
    }
}