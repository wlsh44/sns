package com.example.sns.auth.application;

import com.example.sns.auth.infrastructure.GoogleClient;
import com.example.sns.auth.presentation.dto.TokenResponse;
import com.example.sns.common.fixtures.MemberFixture;
import com.example.sns.common.support.ServiceTest;
import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.sns.common.fixtures.AuthFixture.BASIC_ID_TOKEN;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_EMAIL;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_NAME;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_NICKNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class AuthServiceTest extends ServiceTest {

    @Autowired
    AuthService authService;

    @SpyBean
    GoogleClient googleClient;

    @Autowired
    JwtProvider provider;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("유저 처음 로그인 시 db에 저장")
    void signIn_userNotExist() throws Exception {
        //given
        doReturn(BASIC_ID_TOKEN).when(googleClient).getIdToken(any());

        //when
        TokenResponse tokenResponse = authService.signIn("code");

        //then
        Long result = provider.parseMemberId(tokenResponse.getToken());
        Member member = memberRepository.findAll().get(0);

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(member.getId()).isEqualTo(result);
            softAssertions.assertThat(member.getInfo().getName()).isEqualTo(BASIC_NAME);
            softAssertions.assertThat(member.getInfo().getNickname()).isEqualTo(BASIC_NICKNAME);
            softAssertions.assertThat(member.getInfo().getEmail()).isEqualTo(BASIC_EMAIL);
        });
    }

    @Test
    @Transactional
    @DisplayName("기존 유저 로그인")
    void signIn_userExist() throws Exception {
        //given
        memberRepository.save(MemberFixture.getBasicMember());
        doReturn(BASIC_ID_TOKEN).when(googleClient).getIdToken(any());

        //when
        TokenResponse tokenResponse = authService.signIn("code");

        //then
        Long result = provider.parseMemberId(tokenResponse.getToken());
        List<Member> members = memberRepository.findAll();
        Member member = members.get(0);

        assertThat(members.size()).isEqualTo(1);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(member.getId()).isEqualTo(result);
            softAssertions.assertThat(member.getInfo().getName()).isEqualTo(BASIC_NAME);
            softAssertions.assertThat(member.getInfo().getNickname()).isEqualTo(BASIC_NICKNAME);
            softAssertions.assertThat(member.getInfo().getEmail()).isEqualTo(BASIC_EMAIL);
        });
    }
}