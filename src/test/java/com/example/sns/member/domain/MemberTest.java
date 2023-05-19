package com.example.sns.member.domain;

import com.example.sns.auth.application.dto.OAuthUserInfoDto;
import com.example.sns.follow.domain.Follow;
import com.example.sns.follow.exception.AlreadyFollowException;
import com.example.sns.follow.exception.NotFollowingMemberException;
import com.example.sns.member.exception.InvalidUsernameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.sns.common.fixtures.MemberFixture.BASIC_BIOGRAPHY2;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_EMAIL;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_NAME;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_USERNAME;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_USERNAME2;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_PROFILE2;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_SOCIAL_ID;
import static com.example.sns.common.fixtures.MemberFixture.FOLLOWER_EMAIL;
import static com.example.sns.common.fixtures.MemberFixture.FOLLOWER_NAME;
import static com.example.sns.common.fixtures.MemberFixture.FOLLOWER_SOCIAL_ID;
import static com.example.sns.common.fixtures.MemberFixture.FOLLOWING_EMAIL;
import static com.example.sns.common.fixtures.MemberFixture.FOLLOWING_NAME;
import static com.example.sns.common.fixtures.MemberFixture.FOLLOWING_SOCIAL_ID;
import static com.example.sns.common.fixtures.MemberFixture.getBasicMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class MemberTest {

    @Test
    @DisplayName("유저가 생성되어야 함")
    void createUserFromInfo() throws Exception {
        //given
        OAuthUserInfoDto userInfoDto = new OAuthUserInfoDto(BASIC_NAME, BASIC_EMAIL, BASIC_SOCIAL_ID);

        //when
        Member member = Member.createUserFrom(userInfoDto);

        //then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(member.getDetailedInfo().getName()).isEqualTo(BASIC_NAME);
            softAssertions.assertThat(member.getDetailedInfo().getEmail()).isEqualTo(BASIC_EMAIL);
            softAssertions.assertThat(member.getSocialInfo().getUsername()).isEqualTo(BASIC_USERNAME);
            softAssertions.assertThat(member.getSocialId()).isEqualTo(BASIC_SOCIAL_ID);
        });
    }

    @Test
    @DisplayName("팔로우에 성공해야 함")
    void follow() throws Exception {
        //given
        Member follower = Member.createUserFrom(new OAuthUserInfoDto(FOLLOWER_NAME, FOLLOWER_EMAIL, FOLLOWER_SOCIAL_ID));
        Member following = Member.createUserFrom(new OAuthUserInfoDto(FOLLOWING_NAME, FOLLOWING_EMAIL, FOLLOWING_SOCIAL_ID));
        Follow followTable = Follow.createFollowTable(follower, following);

        //when
        follower.follow(followTable);
      
        //then
        assertThat(follower.getFollowings()).hasSize(1);
    }

    @Test
    @DisplayName("언팔로우에 성공해야 함")
    void unfollow() throws Exception {
        //given
        Member follower = Member.createUserFrom(new OAuthUserInfoDto(FOLLOWER_NAME, FOLLOWER_EMAIL, FOLLOWER_SOCIAL_ID));
        Member following = Member.createUserFrom(new OAuthUserInfoDto(FOLLOWING_NAME, FOLLOWING_EMAIL, FOLLOWING_SOCIAL_ID));
        Follow followTable = Follow.createFollowTable(follower, following);
        follower.follow(followTable);

        //when
        follower.unfollow(followTable);

        //then
        assertThat(follower.getFollowings()).hasSize(0);
    }

    @Test
    @DisplayName("정보가 수정이 되어야 함")
    void updateTest() throws Exception {
        //given
        Member member = getBasicMember();

        //when
        member.update(BASIC_USERNAME2, BASIC_BIOGRAPHY2, BASIC_PROFILE2);

        //then
        assertThat(member.getSocialInfo().getUsername()).isEqualTo(BASIC_USERNAME2);
        assertThat(member.getSocialInfo().getBiography()).isEqualTo(BASIC_BIOGRAPHY2);
        assertThat(member.getSocialInfo().getProfileUrl()).isEqualTo(BASIC_PROFILE2);
    }

    @Test
    @DisplayName("정보가 수정이 되어야 함")
    void updateTest_invalidUsername() throws Exception {
        //given
        Member member = getBasicMember();

        //when
        assertThatThrownBy(() -> member.update("", BASIC_BIOGRAPHY2, BASIC_PROFILE2))
                .isInstanceOf(InvalidUsernameException.class);
    }
}