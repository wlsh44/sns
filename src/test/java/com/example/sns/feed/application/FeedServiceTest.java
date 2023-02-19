package com.example.sns.feed.application;

import com.example.sns.feed.domain.Feed;
import com.example.sns.feed.domain.FeedImage;
import com.example.sns.feed.domain.FeedImageRepository;
import com.example.sns.feed.domain.FeedRepository;
import com.example.sns.imagestore.exception.ImageStoreException;
import com.example.sns.imagestore.infrastructure.ImageStore;
import com.example.sns.member.domain.Member;
import com.example.sns.member.exception.MemberNotFoundException;
import com.example.sns.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.sns.common.fixtures.FeedFixture.BASIC_FEED_CONTENT;
import static com.example.sns.common.fixtures.FeedFixture.FEED_IMAGE_PATH1;
import static com.example.sns.common.fixtures.FeedFixture.FEED_IMAGE_PATH2;
import static com.example.sns.common.fixtures.FeedFixture.getBasicFeedImages;
import static com.example.sns.common.fixtures.FeedFixture.getBasicUploadRequest;
import static com.example.sns.common.fixtures.MemberFixture.getBasicMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Transactional
@SpringBootTest
class FeedServiceTest {

    @Autowired
    FeedService feedService;

    @MockBean
    ImageStore imageStore;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FeedRepository feedRepository;

    @Autowired
    FeedImageRepository feedImageRepository;

    Member member;

    @BeforeEach
    void init() {
        member = memberRepository.save(getBasicMember());
    }

    @Test
    @DisplayName("피드 업로드에 성공해야 함")
    void uploadTest() throws Exception {
        //given
        List<String> imagePaths = List.of(FEED_IMAGE_PATH1, FEED_IMAGE_PATH2);
        given(imageStore.storeFeedImages(any()))
                .willReturn(imagePaths);

        //when
        feedService.uploadFeed(member.getId(), getBasicUploadRequest(), getBasicFeedImages());

        //then
        List<FeedImage> feedImages = feedImageRepository.findAll();
        List<Feed> feeds = feedRepository.findAll();

        assertSoftly(softlyAssertions -> {
            softlyAssertions.assertThat(feedImages.size()).isEqualTo(imagePaths.size());
            softlyAssertions.assertThat(feedImages.get(0).getImagePath()).isEqualTo(FEED_IMAGE_PATH1);
            softlyAssertions.assertThat(feedImages.get(1).getImagePath()).isEqualTo(FEED_IMAGE_PATH2);

            softlyAssertions.assertThat(feeds.size()).isEqualTo(1);
            softlyAssertions.assertThat(feeds.get(0).getContent()).isEqualTo(BASIC_FEED_CONTENT);
        });
    }

    @Test
    @DisplayName("유저가 없으면 예외가 발생해야 함")
    void uploadFailed_memberNotFound() throws Exception {
        //given
        Long notExistMemberId = 9999L;

        //when then
        assertThatThrownBy(() -> feedService.uploadFeed(notExistMemberId, getBasicUploadRequest(), getBasicFeedImages()))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("이미지 저장에 실패하면 예외가 발생해야 함")
    void uploadFailed_imageStoreException() throws Exception {
        //given
        given(imageStore.storeFeedImages(any()))
                .willThrow(new ImageStoreException(new Throwable()));

        //when then
        assertThatThrownBy(() -> feedService.uploadFeed(member.getId(), getBasicUploadRequest(), getBasicFeedImages()))
                .isInstanceOf(ImageStoreException.class);
    }
}