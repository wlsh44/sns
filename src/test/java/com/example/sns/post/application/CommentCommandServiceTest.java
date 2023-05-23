package com.example.sns.post.application;

import com.example.sns.common.support.ServiceTest;
import com.example.sns.member.domain.Member;
import com.example.sns.post.application.dto.NewCommentRequest;
import com.example.sns.post.domain.Comment;
import com.example.sns.post.domain.CommentRepository;
import com.example.sns.post.domain.Post;
import com.example.sns.post.domain.PostRepository;
import com.example.sns.post.exception.CommentNotFoundException;
import com.example.sns.post.exception.EmptyCommentException;
import com.example.sns.post.exception.NotCommentAuthorException;
import com.example.sns.post.exception.PostNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.example.sns.common.fixtures.CommentFixture.BASIC_COMMENT_CONTENT1;
import static com.example.sns.common.fixtures.CommentFixture.getBasicCommentRequest;
import static com.example.sns.common.fixtures.CommentFixture.getEmptyContentCommentRequest;
import static com.example.sns.common.fixtures.MemberFixture.getBasicMember;
import static com.example.sns.common.fixtures.PostFixture.getBasicPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentCommandServiceTest extends ServiceTest {

    @Autowired
    CommentCommandService commentCommandService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Test
    @DisplayName("올바른 댓글을 생성해야 함")
    void create() throws Exception {
        //given
        Member member = memberRepository.save(getBasicMember());
        Post post = postRepository.save(getBasicPost(member));
        NewCommentRequest request = getBasicCommentRequest();

        //when
        commentCommandService.createComment(member.getId(), post.getId(), request);

        //then
        Comment comment = commentRepository.findAll().get(0);
        assertThat(comment.getContent()).isEqualTo(BASIC_COMMENT_CONTENT1);
        assertThat(comment.getPost().getId()).isEqualTo(post.getId());
        assertThat(comment.getAuthor().getId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("댓글 내용이 비었을 경우 예외가 발생해야 함")
    void create_emptyContent() throws Exception {
        //given
        Member member = memberRepository.save(getBasicMember());
        Post post = postRepository.save(getBasicPost(member));
        NewCommentRequest request = getEmptyContentCommentRequest();
        Long memberId = member.getId();
        Long postId = post.getId();

        //when then
        assertThatThrownBy(() -> commentCommandService.createComment(memberId, postId, request))
            .isInstanceOf(EmptyCommentException.class);
    }

    @Test
    @DisplayName("없는 피드에 댓글을 달 경우 예외가 발생해야 함")
    void create_feedNotFound() throws Exception {
        //given
        Member member = memberRepository.save(getBasicMember());
        Post post = postRepository.save(getBasicPost(member));
        Long notExistFeedId = 9999L;
        NewCommentRequest request = getBasicCommentRequest();
        Long memberId = member.getId();

        //when then
        assertThatThrownBy(() -> commentCommandService.createComment(memberId, notExistFeedId, request))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("댓글이 삭제 되어야 함")
    void deleteCommentTest() throws Exception {
        //given
        Member member = memberRepository.save(getBasicMember());
        Post post = postRepository.save(getBasicPost(member));
        commentCommandService.createComment(member.getId(), post.getId(), getBasicCommentRequest());

        //when
        commentCommandService.deleteComment(member.getId(), 1L);

        //then
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).isEmpty();
    }

    @Test
    @DisplayName("없는 댓글일 경우 예외가 발생해야 함")
    void deleteCommentTest_commentNotFound() throws Exception {
        //given
        Member member = memberRepository.save(getBasicMember());
        Post post = postRepository.save(getBasicPost(member));
        Long notExistId = 999L;
        commentCommandService.createComment(member.getId(), post.getId(), getBasicCommentRequest());
        Long memberId = member.getId();

        //when then
        assertThatThrownBy(() -> commentCommandService.deleteComment(memberId, notExistId))
                .isInstanceOf(CommentNotFoundException.class);
    }

    @Test
    @DisplayName("댓글 작성자가 아닐 경우 예외가 발생해야 함")
    void deleteCommentTest_notAuthor() throws Exception {
        //given
        Member member = memberRepository.save(getBasicMember());
        Post post = postRepository.save(getBasicPost(member));
        Long notAuthorId = 999L;
        commentCommandService.createComment(member.getId(), post.getId(), getBasicCommentRequest());

        //when then
        assertThatThrownBy(() -> commentCommandService.deleteComment(notAuthorId, 1L))
                .isInstanceOf(NotCommentAuthorException.class);
    }
}