package com.example.sns.post.application;

import com.example.sns.common.support.ServiceTest;
import com.example.sns.follow.domain.Follow;
import com.example.sns.follow.domain.FollowRepository;
import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import com.example.sns.post.application.dto.PostResponse;
import com.example.sns.post.domain.PostRepository;
import com.example.sns.post.presentiation.dto.MyFeedResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.sns.common.fixtures.MemberFixture.getBasicMember;
import static com.example.sns.common.fixtures.MemberFixture.getBasicMember2;
import static com.example.sns.common.fixtures.MemberFixture.getFollower;
import static com.example.sns.common.fixtures.MemberFixture.getFollowing;
import static com.example.sns.common.fixtures.PostFixture.getBasicPost;
import static com.example.sns.common.fixtures.PostFixture.getBasicPost2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class FeedServiceTest extends ServiceTest {

    @Autowired
    FeedService feedService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    FollowRepository followRepository;

    @Test
    @DisplayName("팔로우한 유저의 피드를 조회해야 함")
    void findMyFeedTest() throws Exception {
        //given
        Member follower = memberRepository.save(getFollower());
        Member notFollowingMember = memberRepository.save(getBasicMember());
        Member following1 = memberRepository.save(getBasicMember2());
        Member following2 = memberRepository.save(getFollowing());
        postRepository.save(getBasicPost(notFollowingMember));
        postRepository.save(getBasicPost(following1));
        postRepository.save(getBasicPost2(following2));
        followRepository.save(Follow.createFollowTable(follower, following1));
        followRepository.save(Follow.createFollowTable(follower, following2));
        Pageable pageable = PageRequest.of(0, 5);

        //when
        MyFeedResponse response = feedService.findMyFeed(follower.getId(), pageable);

        //then
        List<PostResponse> feed = response.getFeed();
        assertThat(feed).hasSize(2);
    }
}