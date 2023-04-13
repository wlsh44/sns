package com.example.sns.post.application;

import com.example.sns.common.support.ServiceTest;
import com.example.sns.member.domain.Member;
import com.example.sns.member.exception.MemberNotFoundException;
import com.example.sns.post.domain.Like;
import com.example.sns.post.domain.LikeRepository;
import com.example.sns.post.domain.Post;
import com.example.sns.post.domain.PostRepository;
import com.example.sns.post.exception.AlreadyLikedPostException;
import com.example.sns.post.exception.PostNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.example.sns.common.fixtures.MemberFixture.getBasicMember;
import static com.example.sns.common.fixtures.MemberFixture.getBasicMember2;
import static com.example.sns.common.fixtures.PostFixture.getBasicPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class LikeServiceTest extends ServiceTest {

    @Autowired
    LikeService likeService;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    PostRepository postRepository;

    @Test
    @DisplayName("좋아요를 성공해야 함")
    void likeTest() throws Exception {
        //given
        Member member1 = memberRepository.save(getBasicMember());
        Post post = postRepository.save(getBasicPost(member1));

        //when
        likeService.like(member1.getId(), post.getId());

        //then
        List<Like> likes = likeRepository.findAll();
        assertThat(likes).hasSize(1);
    }

    @Test
    @DisplayName("유저가 없는 경우 예외가 발생해야 함")
    void likeTest_memberNotFound() throws Exception {
        //given
        Long notExistId = 999L;
        Member member1 = memberRepository.save(getBasicMember());
        Post post = postRepository.save(getBasicPost(member1));

        //when
        assertThatThrownBy(() -> likeService.like(notExistId, post.getId()))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("포스트가 없는 경우 예외가 발생해야 함")
    void likeTest_postNotFound() throws Exception {
        //given
        Long notExistId = 999L;
        Member member1 = memberRepository.save(getBasicMember());

        //when
        assertThatThrownBy(() -> likeService.like(member1.getId(), notExistId))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("이미 좋아요를 누른 게시글일 경우 예외가 발생해야 함")
    void likeTest_alreadyLikedPost() throws Exception {
        //given
        Member member1 = memberRepository.save(getBasicMember());
        Post post = postRepository.save(getBasicPost(member1));
        likeService.like(member1.getId(), post.getId());

        //when
        assertThatThrownBy(() -> likeService.like(member1.getId(), post.getId()))
                .isInstanceOf(AlreadyLikedPostException.class);
    }
}