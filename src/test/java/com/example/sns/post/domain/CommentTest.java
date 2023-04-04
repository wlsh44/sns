package com.example.sns.post.domain;

import com.example.sns.member.domain.Member;
import com.example.sns.post.exception.EmptyCommentException;
import com.example.sns.post.exception.NotCommentOwnerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommentTest {

    @Mock
    Member member;

    @Test
    @DisplayName("댓글이 생성 되어야 함")
    void createTest() throws Exception {
        //given
        String content = "  content  ";

        //when
        Comment comment = Comment.createComment(member, content);

        //then
        assertThat(comment.getContent()).isEqualTo(content);
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"  ", "    "})
    void createTest(String content) throws Exception {
        //given

        //when then
        assertThatThrownBy(() -> Comment.createComment(member, content))
                .isInstanceOf(EmptyCommentException.class);
    }

    @Test
    @DisplayName("댓글 작성자라면 아무 일도 일어나면 안 됨")
    void validateOwnerTest() throws Exception {
        //given
        Long memberId = 1L;
        Comment comment = Comment.createComment(member, "content");
        given(member.getId()).willReturn(memberId);

        //when then
        assertThatNoException().isThrownBy(() -> comment.validateIsOwner(memberId));
    }

    @Test
    @DisplayName("댓글 작성자가 아니라면 예외가 발생해야 함")
    void validateOwnerTest_notOwner() throws Exception {
        //given
        Long notOwnerId = 999L;
        Comment comment = Comment.createComment(member, "content");
        given(member.getId()).willReturn(1L);

        //when then
        assertThatThrownBy(() -> comment.validateIsOwner(notOwnerId))
                .isInstanceOf(NotCommentOwnerException.class);
    }
}