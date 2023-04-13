package com.example.sns.member.application;

import com.example.sns.common.support.ServiceTest;
import com.example.sns.follow.application.FollowService;
import com.example.sns.member.domain.Member;
import com.example.sns.member.exception.MemberNotFoundException;
import com.example.sns.member.presentation.dto.MemberProfileResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.sns.common.fixtures.MemberFixture.getBasicMember;
import static com.example.sns.common.fixtures.MemberFixture.getBasicMember2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest extends ServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    FollowService followService;

    @Test
    @DisplayName("멤버 프로필을 가져와야 함")
    void getProfileTest() throws Exception {
        //given
        Member loginMember = memberRepository.save(getBasicMember());
        Member profileMember = memberRepository.save(getBasicMember2());
        followService.follow(loginMember.getId(), profileMember.getId());

        //when
        MemberProfileResponse response = memberService.getProfile(loginMember.getId(), profileMember.getInfo().getNickname());

        //then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(profileMember.getId()),
                () -> assertThat(response.getName()).isEqualTo(profileMember.getInfo().getName()),
                () -> assertThat(response.getNickname()).isEqualTo(profileMember.getInfo().getNickname()),
                () -> assertThat(response.getBiography()).isEqualTo(null),
                () -> assertThat(response.getProfileImage()).isEqualTo(null),
                () -> assertThat(response.getFollowerCnt()).isEqualTo(1),
                () -> assertThat(response.getFollowingCnt()).isEqualTo(0),
                () -> assertThat(response.isFollowing()).isTrue()
        );
    }

    @Test
    @DisplayName("멤버 프로필을 가져와야 함")
    void getProfileTest_loginMemberNotFound() throws Exception {
        //given
        Long notExistId = 999L;
        Member profileMember = memberRepository.save(getBasicMember2());

        //when
        assertThatThrownBy(() -> memberService.getProfile(notExistId, profileMember.getInfo().getNickname()))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("멤버 프로필을 가져와야 함")
    void getProfileTest_profileMemberNotFound() throws Exception {
        //given
        String notExistName = "notExistName";
        Member loginMember = memberRepository.save(getBasicMember2());

        //when
        assertThatThrownBy(() -> memberService.getProfile(loginMember.getId(), notExistName))
                .isInstanceOf(MemberNotFoundException.class);
    }
}