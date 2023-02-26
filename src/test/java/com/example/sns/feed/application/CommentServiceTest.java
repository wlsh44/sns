package com.example.sns.feed.application;

import com.example.sns.feed.application.dto.NewCommentRequest;
import com.example.sns.feed.domain.Comment;
import com.example.sns.feed.domain.CommentRepository;
import com.example.sns.feed.domain.Feed;
import com.example.sns.feed.domain.FeedRepository;
import com.example.sns.feed.exception.EmptyCommentException;
import com.example.sns.feed.exception.FeedNotFoundException;
import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.sns.common.fixtures.CommentFixture.BASIC_COMMENT_CONTENT;
import static com.example.sns.common.fixtures.CommentFixture.getBasicCommentRequest;
import static com.example.sns.common.fixtures.CommentFixture.getEmptyContentCommentRequest;
import static com.example.sns.common.fixtures.FeedFixture.getBasicFeed;
import static com.example.sns.common.fixtures.MemberFixture.getBasicMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FeedRepository feedRepository;

    @Autowired
    CommentRepository commentRepository;

    Member member;

    Feed feed;

    @BeforeEach
    void init() {
        member = memberRepository.save(getBasicMember());
        feed = feedRepository.save(getBasicFeed(member));
    }

    @Test
    @DisplayName("올바른 댓글을 생성해야 함")
    void create() throws Exception {
        //given
        NewCommentRequest request = getBasicCommentRequest(feed.getId());

        //when
        commentService.createComment(member.getId(), request);

        //then
        Comment comment = commentRepository.findAll().get(0);
        assertThat(comment.getContent()).isEqualTo(BASIC_COMMENT_CONTENT);
        assertThat(comment.getFeed().getId()).isEqualTo(feed.getId());
        assertThat(comment.getMember().getId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("댓글 내용이 비었을 경우 예외가 발생해야 함")
    void create_emptyContent() throws Exception {
        //given
        NewCommentRequest request = getEmptyContentCommentRequest(feed.getId());

        //when then
        assertThatThrownBy(() -> commentService.createComment(member.getId(), request))
            .isInstanceOf(EmptyCommentException.class);
    }

    @Test
    @DisplayName("없는 피드에 댓글을 달 경우 예외가 발생해야 함")
    void create_feedNotFound() throws Exception {
        //given
        Long notExistFeedId = 9999L;
        NewCommentRequest request = getBasicCommentRequest(notExistFeedId);

        //when then
        assertThatThrownBy(() -> commentService.createComment(member.getId(), request))
                .isInstanceOf(FeedNotFoundException.class);
    }
}