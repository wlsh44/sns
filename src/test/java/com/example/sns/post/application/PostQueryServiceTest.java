package com.example.sns.post.application;

import com.example.sns.common.support.ServiceTest;
import com.example.sns.common.infrastructure.imagestore.ImageStore;
import com.example.sns.like.application.LikeService;
import com.example.sns.member.domain.Member;
import com.example.sns.post.presentiation.dto.PostResponse;
import com.example.sns.post.domain.CommentRepository;
import com.example.sns.post.domain.Post;
import com.example.sns.post.domain.PostRepository;
import com.example.sns.post.exception.PostNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.sns.common.fixtures.MemberFixture.BASIC_USERNAME;
import static com.example.sns.common.fixtures.MemberFixture.getBasicMember;
import static com.example.sns.common.fixtures.MemberFixture.getBasicMember2;
import static com.example.sns.common.fixtures.PostFixture.BASIC_POST_CONTENT;
import static com.example.sns.common.fixtures.PostFixture.getBasicPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    CommentCommandService commentCommandService;

    @Test
    @DisplayName("특정 게시글을 조회해야 함")
    void findPost() throws Exception {
        //given
        Member author = memberRepository.save(getBasicMember());
        Post post = postRepository.save(getBasicPost(author));
        LocalDateTime createdAt = post.getCreatedAt();
        Member member = memberRepository.save(getBasicMember2());
        likeService.like(member.getId(), post.getId());
        PostResponse expect = new PostResponse(post.getId(), author.getId(), BASIC_USERNAME, author.getSocialInfo().getProfileUrl(), List.of(), 1, BASIC_POST_CONTENT, createdAt, true);

        //when
        PostResponse response = postQueryService.findPost(member.getId(), post.getId());

        //then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expect);
    }

    @Test
    @DisplayName("게시글이 없는 경우 조회를 하면 예외가 발생해야 함")
    void findPost_postNotFound() throws Exception {
        //given
        Long notExistId = 999L;
        Member author = memberRepository.save(getBasicMember());
        Long authorId = author.getId();

        //when
        assertThatThrownBy(() -> postQueryService.findPost(authorId, notExistId))
                .isInstanceOf(PostNotFoundException.class);
    }
}