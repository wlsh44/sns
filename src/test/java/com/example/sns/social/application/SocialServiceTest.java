package com.example.sns.social.application;

import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import com.example.sns.member.exception.MemberNotFoundException;
import com.example.sns.social.application.dto.FollowRequest;
import com.example.sns.social.application.dto.UnfollowRequest;
import com.example.sns.social.domain.Follow;
import com.example.sns.social.domain.FollowRepository;
import com.example.sns.social.exception.AlreadyFollowException;
import com.example.sns.social.exception.NotFollowingMemberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.sns.common.fixtures.MemberFixture.getBasicMember;
import static com.example.sns.common.fixtures.MemberFixture.getFollower;
import static com.example.sns.common.fixtures.MemberFixture.getFollowing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class SocialServiceTest {

    @Autowired
    SocialService socialService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FollowRepository followRepository;

    Member member;

    @BeforeEach
    void init() {
        member = getFollower();
        memberRepository.save(member);
    }

    @Test
    @DisplayName("팔로우를 하면 member와 follow 모두 해당 정보를 갖고 있어야 함")
    void follow() throws Exception {
        //given
        Member following = getFollowing();
        following = memberRepository.save(following);
        FollowRequest request = new FollowRequest(following.getId());

        //when
        socialService.follow(member.getId(), request);

        //then
        Follow follow = followRepository.findAll().get(0);
        assertThat(follow.getFollower().getId()).isEqualTo(member.getId());
        assertThat(follow.getFollowing().getId()).isEqualTo(following.getId());
        assertThat(member.getFollowings().contains(follow)).isTrue();
    }

    @Test
    @DisplayName("없는 유저를 팔로우 하면 예외가 발생해야 함")
    void follow_memberNotFound() throws Exception {
        //given
        Long notExistMemberId = 999L;
        FollowRequest request = new FollowRequest(notExistMemberId);


        //when then
        assertThatThrownBy(() -> socialService.follow(member.getId(), request))
            .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void follow_alreadyFollow() throws Exception {
        //given
        Member following = getFollowing();
        following = memberRepository.save(following);
        FollowRequest request = new FollowRequest(following.getId());
        socialService.follow(member.getId(), request);

        //when
        assertThatThrownBy(() -> socialService.follow(member.getId(), request))
                .isInstanceOf(AlreadyFollowException.class);
    }

    @Test
    @DisplayName("언팔로우를 하면 member와 follow 모두 삭제되어야 함")
    void unfollow() throws Exception {
        //given
        Member following = getFollowing();
        following = memberRepository.save(following);
        socialService.follow(member.getId(), new FollowRequest(following.getId()));
        UnfollowRequest request = new UnfollowRequest(following.getId());

        //when
        socialService.unfollow(member.getId(), request);

        //then
        List<Follow> follow = followRepository.findAll();
        assertThat(follow.size()).isEqualTo(0);
        assertThat(member.getFollowings().size()).isEqualTo(0);
    }


    @Test
    @DisplayName("없는 유저를 팔로우 하면 예외가 발생해야 함")
    void unfollow_memberNotFound() throws Exception {
        //given
        Long notExistMemberId = 999L;
        UnfollowRequest request = new UnfollowRequest(notExistMemberId);


        //when then
        assertThatThrownBy(() -> socialService.unfollow(member.getId(), request))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void unfollow_notFollowingMember() throws Exception {
        //given
        Member basicMember = getBasicMember();
        basicMember = memberRepository.save(basicMember);
        UnfollowRequest request = new UnfollowRequest(basicMember.getId());

        //when
        assertThatThrownBy(() -> socialService.unfollow(member.getId(), request))
                .isInstanceOf(NotFollowingMemberException.class);
    }
}