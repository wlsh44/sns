package com.example.sns.post.application;

import com.example.sns.common.support.ServiceTest;
import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import com.example.sns.post.domain.Comment;
import com.example.sns.post.domain.CommentRepository;
import com.example.sns.post.domain.Post;
import com.example.sns.post.domain.PostRepository;
import com.example.sns.post.presentiation.dto.CommentResponse;
import com.example.sns.post.presentiation.dto.CommentsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.sns.common.fixtures.CommentFixture.getBasicComment1;
import static com.example.sns.common.fixtures.CommentFixture.getBasicComment2;
import static com.example.sns.common.fixtures.MemberFixture.getBasicMember;
import static com.example.sns.common.fixtures.MemberFixture.getBasicMember2;
import static com.example.sns.common.fixtures.PostFixture.getBasicPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentQueryServiceTest extends ServiceTest {

    @Autowired
    CommentQueryService commentQueryService;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;
    
    @Test
    @DisplayName("특정 게시글의 댓글들을 조회해야 함")
    void findCommentsTest() throws Exception {
        //given
        Member postAuthor = memberRepository.save(getBasicMember());
        Member commentAuthor = memberRepository.save(getBasicMember2());
        Post post = postRepository.save(getBasicPost(postAuthor));
        CommentsResponse expect = initCommentFixtureAndGetExpect(postAuthor, commentAuthor, post);
        Pageable pageable = PageRequest.of(0, 20);

        //when
        CommentsResponse response = commentQueryService.findComments(post.getId(), pageable);

        //then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expect);
    }

    private CommentsResponse initCommentFixtureAndGetExpect(Member postAuthor, Member commentAuthor, Post post) {
        Comment commentByCommentAuthor1 = commentRepository.save(getBasicComment1(commentAuthor, post));
        Comment commentByPostAuthor = commentRepository.save(getBasicComment2(postAuthor, post));
        Comment commentByCommentAuthor2 = commentRepository.save(getBasicComment2(commentAuthor, post));
        return new CommentsResponse(
                List.of(
                        getCommentResponse(commentAuthor, commentByCommentAuthor2),
                        getCommentResponse(postAuthor, commentByPostAuthor),
                        getCommentResponse(commentAuthor, commentByCommentAuthor1)
                ),
                true,
                0
        );
    }

    private CommentResponse getCommentResponse(Member commentAuthor, Comment commentByCommentAuthor2) {
        return new CommentResponse(
                commentByCommentAuthor2.getId(),
                commentAuthor.getId(),
                commentAuthor.getProfileUrl(),
                commentAuthor.getInfo().getUsername(),
                commentByCommentAuthor2.getContent(),
                commentByCommentAuthor2.getCreatedAt()
        );
    }
}