package com.example.sns.post.presentiation;

import com.example.sns.common.support.MockControllerTest;
import com.example.sns.post.application.dto.NewCommentRequest;
import com.example.sns.post.exception.CommentNotFoundException;
import com.example.sns.post.exception.EmptyCommentException;
import com.example.sns.post.exception.NotCommentAuthorException;
import com.example.sns.post.exception.PostNotFoundException;
import com.example.sns.post.presentiation.dto.CommentsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

import static com.example.sns.common.fixtures.AuthFixture.ACCESS_TOKEN;
import static com.example.sns.common.fixtures.CommentFixture.getBasicCommentRequest;
import static com.example.sns.common.fixtures.CommentFixture.getEmptyContentCommentRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends MockControllerTest {

    @Test
    @DisplayName("댓글을 생성하면 200 읃답을 해야 함")
    void createTest() throws Exception {
        //given
        NewCommentRequest request = getBasicCommentRequest();

        //when then
        mockMvc.perform(post("/posts/1/comments")
                        .param("postId", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("없는 피드에 댓글을 달 경우 400 예외를 응답해야 함")
    void createTest_feedNotFound() throws Exception {
        //given
        NewCommentRequest request = getBasicCommentRequest();
        doThrow(new PostNotFoundException(1L))
                .when(commentCommandService)
                .createComment(any(), any(), any());

        //when then
        mockMvc.perform(post("/posts/1/comments")
                        .param("postId", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("댓글 내용이 비었을 경우 400 예외를 응답해야 함")
    void createTest_emptyContent() throws Exception {
        //given
        NewCommentRequest request = getEmptyContentCommentRequest();
        doThrow(new EmptyCommentException())
                .when(commentCommandService)
                .createComment(any(), any(), any());

        //when then
        mockMvc.perform(post("/posts/1/comments")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("댓글 삭제에 성공할 경우 200을 응답해야 함")
    void deleteTest() throws Exception {
        //given
        doNothing().when(commentCommandService)
                .deleteComment(any(), any());

        //when then
        mockMvc.perform(delete("/posts/1/comments/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("댓글이 없을 경우 400을 응답해야 함")
    void deleteTest_commentNotFound() throws Exception {
        //given
        doThrow(new CommentNotFoundException(1L)).when(commentCommandService)
                .deleteComment(any(), any());

        //when then
        mockMvc.perform(delete("/posts/1/comments/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("댓글 작성자가 아닐 경우 400을 응답해야 함")
    void deleteTest_notAuthor() throws Exception {
        //given
        doThrow(new NotCommentAuthorException()).when(commentCommandService)
                .deleteComment(any(), any());

        //when then
        mockMvc.perform(delete("/posts/1/comments/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("게시글 댓글을 조회할 경우 올바른 데이터와 200을 응답해야 함")
    void findCommentsTest() throws Exception {
        //given
        CommentsResponse response = new CommentsResponse(List.of(), true, 0);
        given(commentQueryService.findComments(any(), any()))
                .willReturn(response);

        //when then
        mockMvc.perform(get("/posts/1/comments")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }
}