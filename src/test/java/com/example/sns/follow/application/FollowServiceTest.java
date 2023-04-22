package com.example.sns.follow.application;

import com.example.sns.common.support.ServiceTest;
import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import com.example.sns.member.exception.MemberNotFoundException;
import com.example.sns.follow.domain.Follow;
import com.example.sns.follow.domain.FollowRepository;
import com.example.sns.follow.exception.AlreadyFollowException;
import com.example.sns.follow.exception.NotFollowingMemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.example.sns.common.fixtures.MemberFixture.getBasicMember;
import static com.example.sns.common.fixtures.MemberFixture.getBasicMember2;
import static com.example.sns.common.fixtures.MemberFixture.getFollower;
import static com.example.sns.common.fixtures.MemberFixture.getFollowing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FollowServiceTest extends ServiceTest {

    @Autowired
    FollowService followService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FollowRepository followRepository;

    @Test
    @DisplayName("팔로우를 하면 member와 follow 모두 해당 정보를 갖고 있어야 함")
    void follow() throws Exception {
        //given
        Member follower = memberRepository.save(getFollower());
        Member following = memberRepository.save(getFollowing());

        //when
        followService.follow(follower.getId(), following.getId());

        //then
        Follow follow = followRepository.findAll().get(0);
        assertThat(follow.getFollower().getId()).isEqualTo(follower.getId());
        assertThat(follow.getFollowing().getId()).isEqualTo(following.getId());
    }

    @Test
    @DisplayName("없는 유저를 팔로우 하면 예외가 발생해야 함")
    void follow_memberNotFound() throws Exception {
        //given
        Member member = memberRepository.save(getBasicMember());
        Long notExistMemberId = 999L;

        //when then
        assertThatThrownBy(() -> followService.follow(member.getId(), notExistMemberId))
            .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("이미 팔로우 한 유저인 경우 예외가 발생해야 함")
    void follow_alreadyFollow() throws Exception {
        //given
        Member follower = memberRepository.save(getFollower());
        Member following = memberRepository.save(getFollowing());
        followService.follow(follower.getId(), following.getId());

        //when
        assertThatThrownBy(() -> followService.follow(follower.getId(), following.getId()))
                .isInstanceOf(AlreadyFollowException.class);
    }

    @Test
    @DisplayName("언팔로우를 하면 follow가 삭제되어야 함")
    void unfollow() throws Exception {
        //given
        Member follower = memberRepository.save(getFollower());
        Member following = memberRepository.save(getFollowing());
        followService.follow(follower.getId(), following.getId());

        //when
        followService.unfollow(follower.getId(), following.getId());

        //then
        List<Follow> follow = followRepository.findAll();
        assertThat(follow.size()).isEqualTo(0);
    }


    @Test
    @DisplayName("없는 유저를 팔로우 하면 예외가 발생해야 함")
    void unfollow_memberNotFound() throws Exception {
        //given
        Member member = memberRepository.save(getBasicMember());
        Long notExistMemberId = 999L;

        //when then
        assertThatThrownBy(() -> followService.unfollow(member.getId(), notExistMemberId))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("팔로우하지 않은 유저를 언팔로우 하면 예외가 발생해야 함")
    void unfollow_notFollowingMember() throws Exception {
        //given
        Member member1 = memberRepository.save(getBasicMember());
        Member member2 = memberRepository.save(getBasicMember2());

        //when
        assertThatThrownBy(() -> followService.unfollow(member1.getId(), member2.getId()))
                .isInstanceOf(NotFollowingMemberException.class);
    }
}