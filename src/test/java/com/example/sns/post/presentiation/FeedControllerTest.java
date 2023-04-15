package com.example.sns.post.presentiation;

import com.example.sns.common.support.MockControllerTest;
import com.example.sns.post.application.dto.PostResponse;
import com.example.sns.post.presentiation.dto.MyFeedResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.time.LocalDate;
import java.util.List;

import static com.example.sns.common.fixtures.AuthFixture.ACCESS_TOKEN;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_NICKNAME;
import static com.example.sns.common.fixtures.PostFixture.BASIC_POST_CONTENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FeedControllerTest extends MockControllerTest {

    @Test
    @DisplayName("내 피드 조회에 성공하면 데이터와 200 응답을 함")
    void findMyFeedTest() throws Exception {
        //given
        MyFeedResponse response = new MyFeedResponse(List.of(new PostResponse(1L, BASIC_NICKNAME, List.of("url"), 0, BASIC_POST_CONTENT, LocalDate.now(), false)));
        given(feedService.findMyFeed(any(), any()))
                .willReturn(response);

        mockMvc.perform(get("/feed/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }
}