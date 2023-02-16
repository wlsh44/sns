package com.example.sns.user.domain;

import com.example.sns.auth.application.dto.OAuthUserInfoDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MemberTest {

    @Test
    void createUserFromInfo() throws Exception {
        //given
        OAuthUserInfoDto userInfoDto = mock(OAuthUserInfoDto.class);
        String email = "nickname@test.test";
        String socialId = "12345";
        String userName = "test";
        given(userInfoDto.getEmail()).willReturn(email);
        given(userInfoDto.getSocialId()).willReturn(socialId);
        given(userInfoDto.getName()).willReturn(userName);

        //when
        Member member = Member.createUserFrom(userInfoDto);

        //then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(member.getInfo().getUserName()).isEqualTo(userName);
            softAssertions.assertThat(member.getInfo().getEmail()).isEqualTo(email);
            softAssertions.assertThat(member.getInfo().getNickName()).isEqualTo("nickname");
            softAssertions.assertThat(member.getSocialId()).isEqualTo(socialId);
        });
    }
}