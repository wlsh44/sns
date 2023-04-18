package com.example.sns.post.presentiation;

import com.example.sns.common.support.MockControllerTest;
import com.example.sns.post.presentiation.dto.PostResponse;
import com.example.sns.post.presentiation.dto.MyFeedResponse;
import com.example.sns.post.presentiation.dto.RecentFeedPostResponse;
import com.example.sns.post.presentiation.dto.RecentFeedResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.time.LocalDate;
import java.util.List;

import static com.example.sns.common.fixtures.AuthFixture.ACCESS_TOKEN;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_NICKNAME;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_PROFILE;
import static com.example.sns.common.fixtures.PostFixture.BASIC_POST_CONTENT;
import static com.example.sns.common.fixtures.PostFixture.POST_IMAGE_PATH1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        MyFeedResponse response = new MyFeedResponse(List.of(new PostResponse(1L, 1L, BASIC_NICKNAME, BASIC_PROFILE, List.of("url"), 0, BASIC_POST_CONTENT, LocalDate.now(), false)), false, 0);
        given(feedService.findMyFeed(any(), any()))
                .willReturn(response);

        mockMvc.perform(get("/feed/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName("최신 피드 조회에 성공하면 데이터와 200 응답을 함")
    void findRecentFeedTest() throws Exception {
        //given
        RecentFeedResponse response = new RecentFeedResponse(List.of(new RecentFeedPostResponse(1L, POST_IMAGE_PATH1)), false, 0);
        given(feedService.findRecentFeed(any()))
                .willReturn(response);

        mockMvc.perform(get("/feed/timeline")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }
}