package com.example.sns.auth.application;

import com.example.sns.auth.infrastructure.GoogleClient;
import com.example.sns.auth.infrastructure.JwtProvider;
import com.example.sns.auth.infrastructure.dto.GoogleAuthResponse;
import com.example.sns.auth.presentation.dto.TokenResponse;
import com.example.sns.common.fixtures.MemberFixture;
import com.example.sns.user.domain.Member;
import com.example.sns.user.infrastructure.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static com.example.sns.common.fixtures.AuthFixture.BASIC_ID_TOKEN;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_EMAIL;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_NAME;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_NICKNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    AuthService authService;

    @SpyBean
    GoogleClient googleClient;

    @Autowired
    JwtProvider provider;

    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
    @DisplayName("유저 처음 로그인 시 db에 저장")
    void signIn_userNotExist() throws Exception {
        //given
        doReturn(BASIC_ID_TOKEN).when(googleClient).getIdToken(any());

        //when
        TokenResponse tokenResponse = authService.signIn("code");

        //then
        Long result = provider.parseUserId(tokenResponse.getToken());
        Member member = userRepository.findAll().get(0);

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(member.getId()).isEqualTo(result);
            softAssertions.assertThat(member.getInfo().getUserName()).isEqualTo(BASIC_NAME);
            softAssertions.assertThat(member.getInfo().getNickName()).isEqualTo(BASIC_NICKNAME);
            softAssertions.assertThat(member.getInfo().getEmail()).isEqualTo(BASIC_EMAIL);
        });
    }

    @Test
    @Transactional
    @DisplayName("기존 유저 로그인")
    void signIn_userExist() throws Exception {
        //given
        userRepository.save(MemberFixture.getBasicMember());
        doReturn(BASIC_ID_TOKEN).when(googleClient).getIdToken(any());

        //when
        TokenResponse tokenResponse = authService.signIn("code");

        //then
        Long result = provider.parseUserId(tokenResponse.getToken());
        List<Member> members = userRepository.findAll();
        Member member = members.get(0);

        assertThat(members.size()).isEqualTo(1);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(member.getId()).isEqualTo(result);
            softAssertions.assertThat(member.getInfo().getUserName()).isEqualTo(BASIC_NAME);
            softAssertions.assertThat(member.getInfo().getNickName()).isEqualTo(BASIC_NICKNAME);
            softAssertions.assertThat(member.getInfo().getEmail()).isEqualTo(BASIC_EMAIL);
        });
    }
}