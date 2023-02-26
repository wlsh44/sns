package com.example.sns.feed.application;

import com.example.sns.feed.domain.Comment;
import com.example.sns.feed.domain.CommentRepository;
import com.example.sns.feed.domain.Feed;
import com.example.sns.feed.domain.FeedImage;
import com.example.sns.feed.domain.FeedRepository;
import com.example.sns.feed.exception.FeedNotFoundException;
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
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.sns.common.fixtures.CommentFixture.getBasicComment;
import static com.example.sns.common.fixtures.CommentFixture.getBasicCommentRequest;
import static com.example.sns.common.fixtures.FeedFixture.BASIC_FEED_CONTENT;
import static com.example.sns.common.fixtures.FeedFixture.BASIC_FEED_IMAGE2;
import static com.example.sns.common.fixtures.FeedFixture.EDIT_FEED_CONTENT;
import static com.example.sns.common.fixtures.FeedFixture.FEED_IMAGE_PATH1;
import static com.example.sns.common.fixtures.FeedFixture.FEED_IMAGE_PATH2;
import static com.example.sns.common.fixtures.FeedFixture.getBasicFeedImages;
import static com.example.sns.common.fixtures.FeedFixture.getBasicUpdateRequest;
import static com.example.sns.common.fixtures.FeedFixture.getBasicUploadRequest;
import static com.example.sns.common.fixtures.MemberFixture.getBasicMember;
import static com.example.sns.common.fixtures.MemberFixture.getBasicMember2;
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
    EntityManager em;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentService commentService;

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
        List<FeedImage> feedImages = getFeedImages();
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

    @Test
    @DisplayName("피드를 수정하면 수정한 데이터를 갖고 있어야 함")
    void editFeed() throws Exception {
        //given
        Feed feed = feedRepository.save(new Feed(member, BASIC_FEED_CONTENT));
        feed.updateFeedImage(List.of(new FeedImage(FEED_IMAGE_PATH1, feed)));
        given(imageStore.storeFeedImages(any()))
                .willReturn(List.of(FEED_IMAGE_PATH2));

        //when
        feedService.updateFeed(member.getId(), feed.getId(), getBasicUpdateRequest(), List.of(BASIC_FEED_IMAGE2));

        //then
        assertThat(feed.getImages().get(0).getImagePath()).isEqualTo(FEED_IMAGE_PATH2);
        assertThat(feed.getContent()).isEqualTo(EDIT_FEED_CONTENT);
    }

    @Test
    @DisplayName("없는 피드를 수정하려고 하면 예외가 발생해야 함")
    void editFeed_feedNotFound() throws Exception {
        //given
        Long notExistFeedId = 1L;

        //when then
        assertThatThrownBy(() -> feedService.updateFeed(member.getId(), notExistFeedId, getBasicUpdateRequest(), List.of(BASIC_FEED_IMAGE2)))
            .isInstanceOf(FeedNotFoundException.class);
    }

    @Test
    @DisplayName("피드를 지우면 피드가 삭제되어야 함")
    void delete() throws Exception {
        //given
        Feed feed = new Feed(member, BASIC_FEED_CONTENT);
        feed.updateFeedImage(List.of(new FeedImage(FEED_IMAGE_PATH1, feed)));
        feed = feedRepository.save(feed);
        commentService.createComment(member.getId(), getBasicCommentRequest(feed.getId()));

        //when
        feedService.deleteFeed(member.getId(), feed.getId());

        //then
        List<Feed> feedList = feedRepository.findAll();
        List<Comment> comments = commentRepository.findAll();
        List<FeedImage> feedImages = getFeedImages();
        assertThat(comments.size()).isEqualTo(0);
        assertThat(feedList.size()).isEqualTo(0);
        assertThat(feedImages.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("없는 피드를 삭제하려고 할 경우 예외가 발생해야 함")
    void delete_feedNotFound() throws Exception {
        //given
        Long notExistFeedId = 9999L;

        //when then
        assertThatThrownBy(() -> feedService.deleteFeed(member.getId(), notExistFeedId))
                .isInstanceOf(FeedNotFoundException.class);
    }

    @Test
    @DisplayName("유저의 피드가 아닌 피드를 삭제할 경우 예외가 발생해야 함")
    void delete_feedNotFound2() throws Exception {
        //given
        Member member2 = memberRepository.save(getBasicMember2());
        Feed feed = feedRepository.save(new Feed(member2, BASIC_FEED_CONTENT));

        //when then
        assertThatThrownBy(() -> feedService.deleteFeed(member.getId(), feed.getId()))
                .isInstanceOf(FeedNotFoundException.class);
    }

    private List<FeedImage> getFeedImages() {
        return em.createQuery("select fi from FeedImage fi", FeedImage.class)
                .getResultList();
    }
}