package com.example.sns.post.application;

import com.example.sns.post.domain.Comment;
import com.example.sns.post.domain.CommentRepository;
import com.example.sns.post.domain.Post;
import com.example.sns.post.domain.PostImage;
import com.example.sns.post.domain.PostRepository;
import com.example.sns.post.exception.PostNotFoundException;
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

import javax.persistence.EntityManager;
import java.util.List;

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
class PostServiceTest {

    @Autowired
    PostService postService;

    @MockBean
    ImageStore imageStore;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

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
        postService.uploadPost(member.getId(), getBasicUploadRequest(), getBasicFeedImages());

        //then
        List<PostImage> postImages = getFeedImages();
        List<Post> posts = postRepository.findAll();
        assertSoftly(softlyAssertions -> {
            softlyAssertions.assertThat(postImages.size()).isEqualTo(imagePaths.size());
            softlyAssertions.assertThat(postImages.get(0).getImagePath()).isEqualTo(FEED_IMAGE_PATH1);
            softlyAssertions.assertThat(postImages.get(1).getImagePath()).isEqualTo(FEED_IMAGE_PATH2);

            softlyAssertions.assertThat(posts.size()).isEqualTo(1);
            softlyAssertions.assertThat(posts.get(0).getContent()).isEqualTo(BASIC_FEED_CONTENT);
        });
    }

    @Test
    @DisplayName("유저가 없으면 예외가 발생해야 함")
    void uploadFailed_memberNotFound() throws Exception {
        //given
        Long notExistMemberId = 9999L;

        //when then
        assertThatThrownBy(() -> postService.uploadPost(notExistMemberId, getBasicUploadRequest(), getBasicFeedImages()))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("이미지 저장에 실패하면 예외가 발생해야 함")
    void uploadFailed_imageStoreException() throws Exception {
        //given
        given(imageStore.storeFeedImages(any()))
                .willThrow(new ImageStoreException(new Throwable()));

        //when then
        assertThatThrownBy(() -> postService.uploadPost(member.getId(), getBasicUploadRequest(), getBasicFeedImages()))
                .isInstanceOf(ImageStoreException.class);
    }

    @Test
    @DisplayName("피드를 수정하면 수정한 데이터를 갖고 있어야 함")
    void editFeed() throws Exception {
        //given
        Post post = postRepository.save(new Post(member, BASIC_FEED_CONTENT));
        post.updatePostImage(List.of(new PostImage(FEED_IMAGE_PATH1, post)));
        given(imageStore.storeFeedImages(any()))
                .willReturn(List.of(FEED_IMAGE_PATH2));

        //when
        postService.updatePost(member.getId(), post.getId(), getBasicUpdateRequest(), List.of(BASIC_FEED_IMAGE2));

        //then
        assertThat(post.getImages().get(0).getImagePath()).isEqualTo(FEED_IMAGE_PATH2);
        assertThat(post.getContent()).isEqualTo(EDIT_FEED_CONTENT);
    }

    @Test
    @DisplayName("없는 피드를 수정하려고 하면 예외가 발생해야 함")
    void editFeed_feedNotFound() throws Exception {
        //given
        Long notExistFeedId = 1L;

        //when then
        assertThatThrownBy(() -> postService.updatePost(member.getId(), notExistFeedId, getBasicUpdateRequest(), List.of(BASIC_FEED_IMAGE2)))
            .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("피드를 지우면 피드가 삭제되어야 함")
    void delete() throws Exception {
        //given
        Post post = new Post(member, BASIC_FEED_CONTENT);
        post.updatePostImage(List.of(new PostImage(FEED_IMAGE_PATH1, post)));
        post = postRepository.save(post);
        commentService.createComment(member.getId(), getBasicCommentRequest(post.getId()));

        //when
        postService.deletePost(member.getId(), post.getId());

        //then
        List<Post> postList = postRepository.findAll();
        List<Comment> comments = commentRepository.findAll();
        List<PostImage> postImages = getFeedImages();
        assertThat(comments.size()).isEqualTo(0);
        assertThat(postList.size()).isEqualTo(0);
        assertThat(postImages.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("없는 피드를 삭제하려고 할 경우 예외가 발생해야 함")
    void delete_feedNotFound() throws Exception {
        //given
        Long notExistFeedId = 9999L;

        //when then
        assertThatThrownBy(() -> postService.deletePost(member.getId(), notExistFeedId))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("유저의 피드가 아닌 피드를 삭제할 경우 예외가 발생해야 함")
    void delete_feedNotFound2() throws Exception {
        //given
        Member member2 = memberRepository.save(getBasicMember2());
        Post post = postRepository.save(new Post(member2, BASIC_FEED_CONTENT));

        //when then
        assertThatThrownBy(() -> postService.deletePost(member.getId(), post.getId()))
                .isInstanceOf(PostNotFoundException.class);
    }

    private List<PostImage> getFeedImages() {
        return em.createQuery("select fi from PostImage fi", PostImage.class)
                .getResultList();
    }
}