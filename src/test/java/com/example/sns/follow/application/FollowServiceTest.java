package com.example.sns.follow.application;

import com.example.sns.common.support.ServiceTest;
import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import com.example.sns.member.exception.MemberNotFoundException;
import com.example.sns.follow.application.dto.FollowRequest;
import com.example.sns.follow.application.dto.UnfollowRequest;
import com.example.sns.follow.domain.Follow;
import com.example.sns.follow.domain.FollowRepository;
import com.example.sns.follow.exception.AlreadyFollowException;
import com.example.sns.follow.exception.NotFollowingMemberException;
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
        FollowRequest request = new FollowRequest(following.getId());

        //when
        followService.follow(follower.getId(), request);

        //then
        Follow follow = followRepository.findAll().get(0);
        assertThat(follow.getFollower().getId()).isEqualTo(follower.getId());
        assertThat(follow.getFollowing().getId()).isEqualTo(following.getId());
    }

    @Test
    @DisplayName("없는 유저를 팔로우 하면 예외가 발생해야 함")
    void follow_memberNotFound() throws Exception {
        //given
        Long notExistMemberId = 999L;
        FollowRequest request = new FollowRequest(notExistMemberId);

        //when then
        assertThatThrownBy(() -> followService.follow(member.getId(), request))
            .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("이미 팔로우 한 유저인 경우 예외가 발생해야 함")
    void follow_alreadyFollow() throws Exception {
        //given
        Member follower = memberRepository.save(getFollower());
        Member following = memberRepository.save(getFollowing());
        FollowRequest request = new FollowRequest(following.getId());
        followService.follow(follower.getId(), request);

        //when
        assertThatThrownBy(() -> followService.follow(follower.getId(), request))
                .isInstanceOf(AlreadyFollowException.class);
    }

    @Test
    @DisplayName("언팔로우를 하면 member와 follow 모두 삭제되어야 함")
    void unfollow() throws Exception {
        //given
        Member following = getFollowing();
        following = memberRepository.save(following);
        followService.follow(member.getId(), new FollowRequest(following.getId()));
        UnfollowRequest request = new UnfollowRequest(following.getId());

        //when
        followService.unfollow(member.getId(), request);

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
        assertThatThrownBy(() -> followService.unfollow(member.getId(), request))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void unfollow_notFollowingMember() throws Exception {
        //given
        Member basicMember = getBasicMember();
        basicMember = memberRepository.save(basicMember);
        UnfollowRequest request = new UnfollowRequest(basicMember.getId());

        //when
        assertThatThrownBy(() -> followService.unfollow(member.getId(), request))
                .isInstanceOf(NotFollowingMemberException.class);
    }
}