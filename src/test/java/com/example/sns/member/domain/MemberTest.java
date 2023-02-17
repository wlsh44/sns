package com.example.sns.member.domain;

import com.example.sns.auth.application.dto.OAuthUserInfoDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.sns.common.fixtures.MemberFixture.BASIC_EMAIL;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_NAME;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_NICKNAME;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_SOCIAL_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MemberTest {

    @Test
    void createUserFromInfo() throws Exception {
        //given
        OAuthUserInfoDto userInfoDto = new OAuthUserInfoDto(BASIC_NAME, BASIC_EMAIL, BASIC_SOCIAL_ID);

        //when
        Member member = Member.createUserFrom(userInfoDto);

        //then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(member.getInfo().getUserName()).isEqualTo(BASIC_NAME);
            softAssertions.assertThat(member.getInfo().getEmail()).isEqualTo(BASIC_EMAIL);
            softAssertions.assertThat(member.getInfo().getNickName()).isEqualTo(BASIC_NICKNAME);
            softAssertions.assertThat(member.getSocialId()).isEqualTo(BASIC_SOCIAL_ID);
        });
    }
}