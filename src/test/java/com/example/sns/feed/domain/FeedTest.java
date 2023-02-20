package com.example.sns.feed.domain;

import com.example.sns.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.example.sns.common.fixtures.FeedFixture.BASIC_FEED_CONTENT;
import static com.example.sns.common.fixtures.FeedFixture.EDIT_FEED_CONTENT;
import static com.example.sns.common.fixtures.FeedFixture.FEED_IMAGE_PATH1;
import static com.example.sns.common.fixtures.FeedFixture.FEED_IMAGE_PATH2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class FeedTest {

    Member member;

    @BeforeEach
    void init() {
        member = mock(Member.class);
    }

    @Test
    @DisplayName("피드 생성")
    void createTest() throws Exception {
        //given

        //when
        Feed feed = Feed.createFeed(member, BASIC_FEED_CONTENT);

        //then
        assertThat(feed.getContent()).isEqualTo(BASIC_FEED_CONTENT);
    }

    @Test
    @DisplayName("피드 생성할 때 내용이 null이면 빈 문자열을 갖고 있어야 함")
    void createTest_nullContent() throws Exception {
        //given

        //when
        Feed feed = Feed.createFeed(member, null);

        //then
        assertThat(feed.getContent()).isEqualTo("");
    }

    @Test
    void editFeed() throws Exception {
        //given
        Feed feed = Feed.createFeed(member, BASIC_FEED_CONTENT);
        FeedImage feedImage = new FeedImage(FEED_IMAGE_PATH1, feed);
        feed.updateFeedImage(List.of(feedImage));

        List<FeedImage> newFeedImages = List.of(new FeedImage(FEED_IMAGE_PATH2, feed));

        //when
        feed.editFeed(EDIT_FEED_CONTENT, newFeedImages);

        //then
        assertThat(feed.getContent()).isEqualTo(EDIT_FEED_CONTENT);
        assertThat(feed.getImages()).isEqualTo(newFeedImages);
    }
}