package com.example.sns.member.domain;

import com.example.sns.auth.application.dto.OAuthUserInfoDto;
import com.example.sns.follow.exception.AlreadyFollowException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.sns.common.fixtures.MemberFixture.BASIC_EMAIL;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_NAME;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_NICKNAME;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_SOCIAL_ID;
import static com.example.sns.common.fixtures.MemberFixture.FOLLOWER_EMAIL;
import static com.example.sns.common.fixtures.MemberFixture.FOLLOWER_NAME;
import static com.example.sns.common.fixtures.MemberFixture.FOLLOWER_SOCIAL_ID;
import static com.example.sns.common.fixtures.MemberFixture.FOLLOWING_EMAIL;
import static com.example.sns.common.fixtures.MemberFixture.FOLLOWING_NAME;
import static com.example.sns.common.fixtures.MemberFixture.FOLLOWING_SOCIAL_ID;
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
            softAssertions.assertThat(member.getInfo().getName()).isEqualTo(BASIC_NAME);
            softAssertions.assertThat(member.getInfo().getEmail()).isEqualTo(BASIC_EMAIL);
            softAssertions.assertThat(member.getInfo().getNickName()).isEqualTo(BASIC_NICKNAME);
            softAssertions.assertThat(member.getSocialId()).isEqualTo(BASIC_SOCIAL_ID);
        });
    }

    @Test
    @DisplayName("팔로우에 성공해야 함")
    void follow() throws Exception {
        //given
        Member follower = Member.createUserFrom(new OAuthUserInfoDto(FOLLOWER_NAME, FOLLOWER_EMAIL, FOLLOWER_SOCIAL_ID));
        Member following = Member.createUserFrom(new OAuthUserInfoDto(FOLLOWING_NAME, FOLLOWING_EMAIL, FOLLOWING_SOCIAL_ID));

        //when
        follower.follow(following);

        //then
        assertThat(follower.getFollowings()).hasSize(1);
    }

    @Test
    @DisplayName("이미 팔로우 한 유저인 경우 예외가 발생해야 함")
    void follow_alreadyFollow() throws Exception {
        //given
        Member follower = Member.createUserFrom(new OAuthUserInfoDto(FOLLOWER_NAME, FOLLOWER_EMAIL, FOLLOWER_SOCIAL_ID));
        Member following = Member.createUserFrom(new OAuthUserInfoDto(FOLLOWING_NAME, FOLLOWING_EMAIL, FOLLOWING_SOCIAL_ID));
        follower.follow(following);

        //when then
        assertThatThrownBy(() -> follower.follow(following))
                .isInstanceOf(AlreadyFollowException.class);
    }
}