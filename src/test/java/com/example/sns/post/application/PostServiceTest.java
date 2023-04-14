package com.example.sns.post.application;

import com.example.sns.common.support.ServiceTest;
import com.example.sns.post.application.dto.PostResponse;
import com.example.sns.post.domain.Comment;
import com.example.sns.post.domain.CommentRepository;
import com.example.sns.post.domain.Post;
import com.example.sns.post.domain.PostImage;
import com.example.sns.post.domain.PostRepository;
import com.example.sns.post.exception.NotAuthorException;
import com.example.sns.post.exception.PostNotFoundException;
import com.example.sns.imagestore.exception.ImageStoreException;
import com.example.sns.imagestore.infrastructure.ImageStore;
import com.example.sns.member.domain.Member;
import com.example.sns.member.exception.MemberNotFoundException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static com.example.sns.common.fixtures.CommentFixture.getBasicCommentRequest;
import static com.example.sns.common.fixtures.MemberFixture.getBasicMember;
import static com.example.sns.common.fixtures.PostFixture.BASIC_POST_CONTENT;
import static com.example.sns.common.fixtures.PostFixture.BASIC_POST_IMAGE2;
import static com.example.sns.common.fixtures.PostFixture.EDIT_POST_CONTENT;
import static com.example.sns.common.fixtures.PostFixture.POST_IMAGE_PATH1;
import static com.example.sns.common.fixtures.PostFixture.POST_IMAGE_PATH2;
import static com.example.sns.common.fixtures.PostFixture.getBasicPost;
import static com.example.sns.common.fixtures.PostFixture.getBasicPostImages;
import static com.example.sns.common.fixtures.PostFixture.getBasicUpdateRequest;
import static com.example.sns.common.fixtures.PostFixture.getBasicUploadRequest;
import static com.example.sns.common.fixtures.MemberFixture.getBasicMember2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class PostServiceTest extends ServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    LikeService likeService;

    @MockBean
    ImageStore imageStore;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentService commentService;

    @Test
    @DisplayName("게시글 업로드에 성공해야 함")
    void uploadTest() throws Exception {
        //given
        List<String> imagePaths = List.of(POST_IMAGE_PATH1, POST_IMAGE_PATH2);
        given(imageStore.storeFeedImages(any()))
                .willReturn(imagePaths);

        //when
        postService.uploadPost(member.getId(), getBasicUploadRequest(), getBasicPostImages());

        //then
        List<PostImage> postImages = getPostImages();
        List<Post> posts = postRepository.findAll();
        assertSoftly(softlyAssertions -> {
            softlyAssertions.assertThat(postImages.size()).isEqualTo(imagePaths.size());
            softlyAssertions.assertThat(postImages.get(0).getImagePath()).isEqualTo(POST_IMAGE_PATH1);
            softlyAssertions.assertThat(postImages.get(1).getImagePath()).isEqualTo(POST_IMAGE_PATH2);

            softlyAssertions.assertThat(posts.size()).isEqualTo(1);
            softlyAssertions.assertThat(posts.get(0).getContent()).isEqualTo(BASIC_POST_CONTENT);
        });
    }

    @Test
    @DisplayName("유저가 없으면 예외가 발생해야 함")
    void uploadFailed_memberNotFound() throws Exception {
        //given
        Long notExistMemberId = 9999L;

        //when then
        assertThatThrownBy(() -> postService.uploadPost(notExistMemberId, getBasicUploadRequest(), getBasicPostImages()))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("이미지 저장에 실패하면 예외가 발생해야 함")
    void uploadFailed_imageStoreException() throws Exception {
        //given
        given(imageStore.storeFeedImages(any()))
                .willThrow(new ImageStoreException(new Throwable()));

        //when then
        assertThatThrownBy(() -> postService.uploadPost(member.getId(), getBasicUploadRequest(), getBasicPostImages()))
                .isInstanceOf(ImageStoreException.class);
    }

    @Test
    @Disabled
    @DisplayName("게시글을 수정하면 수정한 데이터를 갖고 있어야 함")
    void editFeed() throws Exception {
        //given
        Post post = postRepository.save(new Post(member, BASIC_POST_CONTENT));
        post.updatePostImage(List.of(new PostImage(POST_IMAGE_PATH1, post)));
        given(imageStore.storeFeedImages(any()))
                .willReturn(List.of(POST_IMAGE_PATH2));

        //when
        postService.updatePost(member.getId(), post.getId(), getBasicUpdateRequest(), List.of(BASIC_POST_IMAGE2));

        //then
        assertThat(post.getImages().get(0).getImagePath()).isEqualTo(POST_IMAGE_PATH2);
        assertThat(post.getContent()).isEqualTo(EDIT_POST_CONTENT);
    }

    @Test
    @Disabled
    @DisplayName("없는 게시글을 수정하려고 하면 예외가 발생해야 함")
    void editFeed_feedNotFound() throws Exception {
        //given
        Long notExistFeedId = 1L;

        //when then
        assertThatThrownBy(() -> postService.updatePost(member.getId(), notExistFeedId, getBasicUpdateRequest(), List.of(BASIC_POST_IMAGE2)))
            .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("게시글을 지우면 게시글가 삭제되어야 함")
    void delete() throws Exception {
        //given
        Post post = new Post(member, BASIC_POST_CONTENT);
        post.updatePostImage(List.of(new PostImage(POST_IMAGE_PATH1, post)));
        post = postRepository.save(post);
        commentService.createComment(member.getId(), post.getId(), getBasicCommentRequest(post.getId()));

        //when
        postService.deletePost(member.getId(), post.getId());

        //then
        List<Post> postList = postRepository.findAll();
        List<Comment> comments = commentRepository.findAll();
        List<PostImage> postImages = getPostImages();
        assertThat(comments.size()).isEqualTo(0);
        assertThat(postList.size()).isEqualTo(0);
        assertThat(postImages.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("없는 게시글을 삭제하려고 할 경우 예외가 발생해야 함")
    void delete_postNotFound() throws Exception {
        //given
        Long notExistPostId = 9999L;

        //when then
        assertThatThrownBy(() -> postService.deletePost(member.getId(), notExistPostId))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("유저의 게시글가 아닌 게시글을 삭제할 경우 예외가 발생해야 함")
    void delete_notAuthor() throws Exception {
        //given
        Member member2 = memberRepository.save(getBasicMember2());
        Post post = postRepository.save(new Post(member2, BASIC_POST_CONTENT));

        //when then
        assertThatThrownBy(() -> postService.deletePost(member.getId(), post.getId()))
                .isInstanceOf(NotAuthorException.class);
    }

    private List<PostImage> getPostImages() {
        return em.createQuery("select fi from PostImage fi", PostImage.class)
                .getResultList();
    }

    @Test
    @DisplayName("특정 게시글을 조회해야 함")
    void findPost() throws Exception {
        //given
        Member author = memberRepository.save(getBasicMember());
        Post post = postRepository.save(getBasicPost(author));
        Member member = memberRepository.save(getBasicMember2());
        likeService.like(member.getId(), post.getId());

        //when
        PostResponse response = postService.findPost(member.getId(), post.getId());

        //then
        assertAll(
                () -> assertThat(response.getNickname()).isEqualTo(author.getInfo().getNickname()),
                () -> assertThat(response.getLikeCnt()).isEqualTo(1),
                () -> assertThat(response.getContent()).isEqualTo(post.getContent()),
                () -> assertThat(response.getId()).isEqualTo(post.getId()),
                () -> assertThat(response.getCreatedAt()).isEqualTo(post.getCreatedAt().toLocalDate())
        );
    }

    @Test
    @DisplayName("유저가 없는 경우 조회를 하면 예외가 발생해야 함")
    void findPost_memberNotFound() throws Exception {
        //given
        Long notExistId = 999L;
        Member author = memberRepository.save(getBasicMember());
        Post post = postRepository.save(getBasicPost(author));

        //when
        assertThatThrownBy(() -> postService.findPost(notExistId, post.getId()))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("게시글이 없는 경우 조회를 하면 예외가 발생해야 함")
    void findPost_postNotFound() throws Exception {
        //given
        Long notExistId = 999L;
        Member author = memberRepository.save(getBasicMember());

        //when
        assertThatThrownBy(() -> postService.findPost(author.getId(), notExistId))
                .isInstanceOf(PostNotFoundException.class);
    }
}