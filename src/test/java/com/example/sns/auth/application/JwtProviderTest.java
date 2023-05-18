package com.example.sns.auth.application;

import com.example.sns.auth.application.JwtProvider;
import com.example.sns.auth.exception.ExpiredTokenException;
import com.example.sns.auth.exception.InvalidTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtProviderTest {

    JwtProvider provider;
    String secretKey;
    String issuer;

    @BeforeEach
    void init() {
        secretKey = "secret-key secret-key secret-key secret-key secret-key secret-key secret-key";
        issuer = "test";
    }

    @Test
    @DisplayName("토큰 생성 및 추출")
    void create() throws Exception {
        //given
        provider = new JwtProvider(secretKey, 10000L, issuer);
        String token = provider.createToken(1L);

        //when
        Long memberId = provider.parseMemberId(token);

        //then
        assertThat(memberId).isEqualTo(1L);
    }

    @Test
    @DisplayName("토큰 기간 만료")
    void getUserId() throws Exception {
        //given
        provider = new JwtProvider(secretKey, 0, issuer);
        String token = provider.createToken(1L);

        //when then
        assertThatThrownBy(() -> provider.parseMemberId(token))
                .isInstanceOf(ExpiredTokenException.class);
    }

    @Test
    void invalidToken() throws Exception {
        //given
        provider = new JwtProvider(secretKey, 100L, issuer);
        String token = "token token";

        //when then
        assertThatThrownBy(() -> provider.parseMemberId(token))
                .isInstanceOf(InvalidTokenException.class);
    }
}