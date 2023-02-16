package com.example.sns.user.domain;

import com.example.sns.auth.application.dto.OAuthUserInfoDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class UserTest {

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
        User user = User.createUserFrom(userInfoDto);

        //then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(user.getInfo().getUserName()).isEqualTo(userName);
            softAssertions.assertThat(user.getInfo().getEmail()).isEqualTo(email);
            softAssertions.assertThat(user.getInfo().getNickName()).isEqualTo("nickname");
            softAssertions.assertThat(user.getSocialId()).isEqualTo(socialId);
        });
    }
}