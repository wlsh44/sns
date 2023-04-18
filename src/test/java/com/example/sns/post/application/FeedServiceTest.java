package com.example.sns.post.application;

import com.example.sns.common.support.ServiceTest;
import com.example.sns.follow.domain.Follow;
import com.example.sns.follow.domain.FollowRepository;
import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import com.example.sns.post.presentiation.dto.PostResponse;
import com.example.sns.post.domain.Post;
import com.example.sns.post.domain.PostImage;
import com.example.sns.post.domain.PostRepository;
import com.example.sns.post.presentiation.dto.MyFeedResponse;
import com.example.sns.post.presentiation.dto.RecentFeedPostResponse;
import com.example.sns.post.presentiation.dto.RecentFeedResponse;
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
import static com.example.sns.common.fixtures.PostFixture.POST_IMAGE_PATH1;
import static com.example.sns.common.fixtures.PostFixture.POST_IMAGE_PATH2;
import static com.example.sns.common.fixtures.PostFixture.getBasicPost;
import static com.example.sns.common.fixtures.PostFixture.getBasicPost2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
    @DisplayName("팔로우한 유저의 최신 게시글이 담긴 피드를 조회해야 함")
    void findMyFeedTest() throws Exception {
        //given
        Member follower = memberRepository.save(getFollower());
        MyFeedResponse expect = initMyFeedFixtureAndGetExpect(follower);
        Pageable pageable = PageRequest.of(0, 5);

        //when
        MyFeedResponse response = feedService.findMyFeed(follower.getId(), pageable);

        //then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expect);
    }

    private MyFeedResponse initMyFeedFixtureAndGetExpect(Member follower) {
        Member notFollowingMember = memberRepository.save(getBasicMember());
        Member following1 = memberRepository.save(getBasicMember2());
        Member following2 = memberRepository.save(getFollowing());
        followRepository.save(Follow.createFollowTable(follower, following1));
        followRepository.save(Follow.createFollowTable(follower, following2));
        postRepository.save(getBasicPost(notFollowingMember));
        Post following1Post = postRepository.save(getBasicPost(following1));
        Post following2Post = postRepository.save(getBasicPost2(following2));
        return new MyFeedResponse(
                List.of(
                        getPostResponse(following2, following2Post),
                        getPostResponse(following1, following1Post)
                ),
                true,
                0);
    }

    private PostResponse getPostResponse(Member following2, Post following2Post) {
        return new PostResponse(
                following2Post.getId(),
                following2.getId(),
                following2.getInfo().getNickname(),
                following2.getProfileUrl(),
                List.of(),
                0,
                following2Post.getContent(),
                following2Post.getCreatedAt(),
                false);
    }

    @Test
    @DisplayName("가장 최근에 생성된 게시글 순서로 된 피드를 보여줘야 함")
    void findRecentFeedTest() throws Exception {
        //given
        RecentFeedResponse expect = initRecentFeedFixtureAndGetExpect();
        Pageable pageable = PageRequest.of(0, 10);

        //when
        RecentFeedResponse response = feedService.findRecentFeed(pageable);

        //then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expect);
    }

    private RecentFeedResponse initRecentFeedFixtureAndGetExpect() {
        Member member1 = memberRepository.save(getBasicMember2());
        Member member2 = memberRepository.save(getFollowing());
        Post post1 = savePostWithPostImage(getBasicPost(member1), POST_IMAGE_PATH1);
        Post post2 = savePostWithPostImage(getBasicPost2(member2), POST_IMAGE_PATH2);
        return new RecentFeedResponse(
                List.of(
                        new RecentFeedPostResponse(post2.getId(), POST_IMAGE_PATH2),
                        new RecentFeedPostResponse(post1.getId(), POST_IMAGE_PATH1)),
                true,
                0);
    }

    private Post savePostWithPostImage(Post post, String postImagePath) {
        PostImage postImage1 = new PostImage(postImagePath, post);
        post.addPostImage(postImage1);
        return postRepository.save(post);
    }
}