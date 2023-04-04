package com.example.sns.post.domain;

import com.example.sns.member.domain.Member;
import com.example.sns.post.exception.EmptyCommentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

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
}