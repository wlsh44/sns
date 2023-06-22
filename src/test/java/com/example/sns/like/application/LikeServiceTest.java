package com.example.sns.like.application;

import com.example.sns.common.infrastructure.redis.RedisPrefix;
import com.example.sns.common.infrastructure.redis.RedisService;
import com.example.sns.common.support.ServiceTest;
import com.example.sns.like.domain.Like;
import com.example.sns.like.domain.LikeRepository;
import com.example.sns.like.infrastructure.LikeJpaRepository;
import com.example.sns.like.infrastructure.LikeRepositoryImpl;
import com.example.sns.member.domain.Member;
import com.example.sns.member.exception.MemberNotFoundException;
import com.example.sns.post.domain.Post;
import com.example.sns.post.domain.PostRepository;
import com.example.sns.post.exception.AlreadyLikedPostException;
import com.example.sns.post.exception.NotLikedPostException;
import com.example.sns.post.exception.PostNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.sns.common.fixtures.MemberFixture.getBasicMember;
import static com.example.sns.common.fixtures.PostFixture.getBasicPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LikeServiceTest extends ServiceTest {

    @Autowired
    LikeService likeService;

    @Autowired
    RedisService redisService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @AfterEach
    void clear() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    @DisplayName("좋아요를 누르면 좋아요가 생기며 게시글의 likeCount가 하나 올라야 함")
    void likeTest() throws Exception {
        //given
        Member member = memberRepository.save(getBasicMember());
        Post post = postRepository.save(getBasicPost(member));

        //when
        likeService.like(member.getId(), post.getId());

        //then
        List<String> likes = redisService.scanKeys(RedisPrefix.LIKE_PUSH);
        assertThat(likes).hasSize(1);
    }

    @Test
    @DisplayName("같은 유저가 동시에 좋아요를 누를 경우 좋아요가 하나만 생기고 likeCount가 하나만 올라야 함")
    void likeTest_30_sameMembers() throws Exception {
        //given
        int threadCount = 30;
        Member member = memberRepository.save(getBasicMember());
        Post post = postRepository.save(getBasicPost(member));

        ExecutorService executorService = Executors.newFixedThreadPool(8);
        CountDownLatch latch = new CountDownLatch(threadCount);

        //when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    likeService.like(member.getId(), post.getId());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        //then
        List<String> likes = redisService.scanKeys(RedisPrefix.LIKE_PUSH);
        assertThat(likes).hasSize(1);
    }

    @Test
    @DisplayName("100명이 동시에 좋아요를 누를 경우 100개의 좋아요와 likeCount가 100개 올라야 함")
    void likeTest_100_differentMembers() throws Exception {
        //given
        int threadCount = 100;
        Member member = memberRepository.save(getBasicMember());
        Post post = postRepository.save(getBasicPost(member));
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            members.add(memberRepository.save(getBasicMember()));
        }

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        //when
        for (int i = 0; i < threadCount; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    likeService.like(members.get(finalI).getId(), post.getId());
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        //then
        List<String> likes = redisService.scanKeys(RedisPrefix.LIKE_PUSH);
        assertThat(likes).hasSize(threadCount);
    }

    @Test
    @DisplayName("이미 좋아요를 누른 게시글일 경우 좋아요를 누를 때 예외가 발생해야 함")
    void likeTest_alreadyLikedPost() throws Exception {
        //given
        Member member = memberRepository.save(getBasicMember());
        Post post = postRepository.save(getBasicPost(member));
        likeService.like(member.getId(), post.getId());
        Long memberId = member.getId();
        Long postId = post.getId();

        //when
        assertThatThrownBy(() -> likeService.like(memberId, postId))
                .isInstanceOf(AlreadyLikedPostException.class);
    }

    @Test
    @DisplayName("좋아요 취소를 성공해야 함")
    void cancelLikeTest() throws Exception {
        //given
        Member member = memberRepository.save(getBasicMember());
        Post post = postRepository.save(getBasicPost(member));
        likeService.like(member.getId(), post.getId());

        //when
        likeService.cancelLike(member.getId(), post.getId());

        //then
        List<String> likes = redisService.scanKeys(RedisPrefix.LIKE_PUSH);
        assertThat(likes).isEmpty();
    }

    @Test
    @DisplayName("좋아요를 누르지 않은 게시글일 경우 좋아요 취소를 할 떄 예외가 발생해야 함")
    void cancelLikeTest_alreadyLikedPost() throws Exception {
        //given
        Member member = memberRepository.save(getBasicMember());
        Post post = postRepository.save(getBasicPost(member));
        Long memberId = member.getId();
        Long postId = post.getId();

        //when
        assertThatThrownBy(() -> likeService.cancelLike(memberId, postId))
                .isInstanceOf(NotLikedPostException.class);
    }
}
