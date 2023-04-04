package com.example.sns.post.presentiation;

import com.example.sns.common.support.MockControllerTest;
import com.example.sns.common.exception.dto.ErrorResponse;
import com.example.sns.post.application.dto.NewCommentRequest;
import com.example.sns.post.exception.EmptyCommentException;
import com.example.sns.post.exception.PostNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.example.sns.common.fixtures.CommentFixture.getBasicCommentRequest;
import static com.example.sns.common.fixtures.CommentFixture.getEmptyContentCommentRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends MockControllerTest {

    @Test
    @DisplayName("댓글을 생성하면 200 읃답을 해야 함")
    void createTest() throws Exception {
        //given
        NewCommentRequest request = getBasicCommentRequest(1L);

        //when then
        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("없는 피드에 댓글을 달 경우 400 예외를 응답해야 함")
    void createTest_feedNotFound() throws Exception {
        //given
        Long notExistFeedId = 1L;
        ErrorResponse expect = new ErrorResponse(String.format(PostNotFoundException.ERROR_MSG, notExistFeedId));
        NewCommentRequest request = getBasicCommentRequest(notExistFeedId);
        doThrow(new PostNotFoundException(notExistFeedId))
                .when(commentService)
                .createComment(any(), any());

        //when then
        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(mapper.writeValueAsString(expect)));
    }

    @Test
    @DisplayName("댓글 내용이 비었을 경우 400 예외를 응답해야 함")
    void createTest_emptyContent() throws Exception {
        //given
        ErrorResponse expect = new ErrorResponse(EmptyCommentException.ERROR_MSG);
        NewCommentRequest request = getEmptyContentCommentRequest(1L);
        doThrow(new EmptyCommentException())
                .when(commentService)
                .createComment(any(), any());

        //when then
        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(mapper.writeValueAsString(expect)));
    }
}