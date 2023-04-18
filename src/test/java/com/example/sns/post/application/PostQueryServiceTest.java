package com.example.sns.post.application;

import com.example.sns.common.support.ServiceTest;
import com.example.sns.imagestore.infrastructure.ImageStore;
import com.example.sns.member.domain.Member;
import com.example.sns.member.exception.MemberNotFoundException;
import com.example.sns.post.application.dto.PostResponse;
import com.example.sns.post.domain.CommentRepository;
import com.example.sns.post.domain.Post;
import com.example.sns.post.domain.PostRepository;
import com.example.sns.post.exception.PostNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.example.sns.common.fixtures.MemberFixture.getBasicMember;
import static com.example.sns.common.fixtures.MemberFixture.getBasicMember2;
import static com.example.sns.common.fixtures.PostFixture.getBasicPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;

class PostQueryServiceTest extends ServiceTest {

    @Autowired
    PostQueryService postQueryService;

    @Autowired
    LikeService likeService;

    @MockBean
    ImageStore imageStore;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentService commentService;

    @Test
    @DisplayName("특정 게시글을 조회해야 함")
    void findPost() throws Exception {
        //given
        Member author = memberRepository.save(getBasicMember());
        Post post = postRepository.save(getBasicPost(author));
        Member member = memberRepository.save(getBasicMember2());
        likeService.like(member.getId(), post.getId());

        //when
        PostResponse response = postQueryService.findPost(member.getId(), post.getId());

        //then
        assertAll(
                () -> assertThat(response.getAuthorNickname()).isEqualTo(author.getInfo().getNickname()),
                () -> assertThat(response.getLikeCnt()).isEqualTo(1),
                () -> assertThat(response.getContent()).isEqualTo(post.getContent()),
                () -> assertThat(response.getId()).isEqualTo(post.getId()),
                () -> assertThat(response.getCreatedAt()).isEqualTo(post.getCreatedAt().toLocalDate())
        );
    }

    @Test
    @DisplayName("유저가 없는 경우 조회를 하면 예외가 발생해야 함")
    void findPost_memberNotFound() throws Exception {
        //given
        Long notExistId = 999L;
        Member author = memberRepository.save(getBasicMember());
        Post post = postRepository.save(getBasicPost(author));

        //when
        assertThatThrownBy(() -> postQueryService.findPost(notExistId, post.getId()))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("게시글이 없는 경우 조회를 하면 예외가 발생해야 함")
    void findPost_postNotFound() throws Exception {
        //given
        Long notExistId = 999L;
        Member author = memberRepository.save(getBasicMember());

        //when
        assertThatThrownBy(() -> postQueryService.findPost(author.getId(), notExistId))
                .isInstanceOf(PostNotFoundException.class);
    }
}