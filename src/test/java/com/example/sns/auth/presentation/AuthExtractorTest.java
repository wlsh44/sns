package com.example.sns.auth.presentation;

import com.example.sns.auth.exception.AuthExtractException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AuthExtractorTest {

    private static final String AUTHORIZATION_HEADER_TYPE = "Authorization";

    AuthExtractor authExtractor;

    HttpServletRequest request = mock(HttpServletRequest.class);

    @BeforeEach
    void init() {
        authExtractor = new AuthExtractor();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {"Bearrer jwt-token", "Bearer"})
    @DisplayName("추출 실패시 예외 리턴")
    void extractNothingEmpty(String token) {
        //given
        given(request.getHeader(AUTHORIZATION_HEADER_TYPE))
                .willReturn(token);

        //when then
        assertThatThrownBy(() -> authExtractor.extractAuthToken(request))
                .isInstanceOf(AuthExtractException.class);
    }

    @Test
    @DisplayName("토큰 추출")
    void extract() {
        //given
        given(request.getHeader(AUTHORIZATION_HEADER_TYPE))
                .willReturn("Bearer jwt-token");

        //when then
        assertThat(authExtractor.extractAuthToken(request)).isEqualTo("jwt-token");
    }

}