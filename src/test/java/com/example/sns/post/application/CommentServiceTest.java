package com.example.sns.post.application;

import com.example.sns.common.support.ServiceTest;
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

import static com.example.sns.common.fixtures.CommentFixture.BASIC_COMMENT_CONTENT;
import static com.example.sns.common.fixtures.CommentFixture.getBasicCommentRequest;
import static com.example.sns.common.fixtures.CommentFixture.getEmptyContentCommentRequest;
import static com.example.sns.common.fixtures.PostFixture.getBasicPost;
import static com.example.sns.common.fixtures.PostFixture.getBasicPostImages;
import static com.example.sns.common.fixtures.PostFixture.getBasicUploadRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentServiceTest extends ServiceTest {

    @Autowired
    CommentService commentService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    Post post;

    @Test
    @DisplayName("올바른 댓글을 생성해야 함")
    void create() throws Exception {
        //given
        post = postRepository.save(getBasicPost(member));
        NewCommentRequest request = getBasicCommentRequest(post.getId());

        //when
        commentService.createComment(member.getId(), request);

        //then
        Comment comment = commentRepository.findAll().get(0);
        assertThat(comment.getContent()).isEqualTo(BASIC_COMMENT_CONTENT);
        assertThat(comment.getPost().getId()).isEqualTo(post.getId());
        assertThat(comment.getAuthor().getId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("댓글 내용이 비었을 경우 예외가 발생해야 함")
    void create_emptyContent() throws Exception {
        //given
        post = postRepository.save(getBasicPost(member));
        NewCommentRequest request = getEmptyContentCommentRequest(post.getId());

        //when then
        assertThatThrownBy(() -> commentService.createComment(member.getId(), request))
            .isInstanceOf(EmptyCommentException.class);
    }

    @Test
    @DisplayName("없는 피드에 댓글을 달 경우 예외가 발생해야 함")
    void create_feedNotFound() throws Exception {
        //given
        post = postRepository.save(getBasicPost(member));
        Long notExistFeedId = 9999L;
        NewCommentRequest request = getBasicCommentRequest(notExistFeedId);

        //when then
        assertThatThrownBy(() -> commentService.createComment(member.getId(), request))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("댓글이 삭제 되어야 함")
    void deleteCommentTest() throws Exception {
        //given
        post = postRepository.save(getBasicPost(member));
        commentService.createComment(member.getId(), getBasicCommentRequest(post.getId()));

        //when
        commentService.deleteComment(member.getId(), 1L);

        //then
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(0);
    }

    @Test
    @DisplayName("없는 댓글일 경우 예외가 발생해야 함")
    void deleteCommentTest_commentNotFound() throws Exception {
        //given
        post = postRepository.save(getBasicPost(member));
        Long notExistId = 999L;
        commentService.createComment(member.getId(), getBasicCommentRequest(post.getId()));

        //when then
        assertThatThrownBy(() -> commentService.deleteComment(member.getId(), notExistId))
                .isInstanceOf(CommentNotFoundException.class);
    }

    @Test
    @DisplayName("댓글 작성자가 아닐 경우 예외가 발생해야 함")
    void deleteCommentTest_notAuthor() throws Exception {
        //given
        post = postRepository.save(getBasicPost(member));
        Long notAuthorId = 999L;
        commentService.createComment(member.getId(), getBasicCommentRequest(post.getId()));

        //when then
        assertThatThrownBy(() -> commentService.deleteComment(notAuthorId, 1L))
                .isInstanceOf(NotCommentAuthorException.class);
    }
}