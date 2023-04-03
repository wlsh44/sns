package com.example.sns.post.domain;

import com.example.sns.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.example.sns.common.fixtures.FeedFixture.BASIC_FEED_CONTENT;
import static com.example.sns.common.fixtures.FeedFixture.EDIT_FEED_CONTENT;
import static com.example.sns.common.fixtures.FeedFixture.FEED_IMAGE_PATH1;
import static com.example.sns.common.fixtures.FeedFixture.FEED_IMAGE_PATH2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class PostTest {

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
        Post post = Post.createFeed(member, BASIC_FEED_CONTENT);

        //then
        assertThat(post.getContent()).isEqualTo(BASIC_FEED_CONTENT);
    }

    @Test
    @DisplayName("피드 생성할 때 내용이 null이면 빈 문자열을 갖고 있어야 함")
    void createTest_nullContent() throws Exception {
        //given

        //when
        Post post = Post.createFeed(member, null);

        //then
        assertThat(post.getContent()).isEqualTo("");
    }

    @Test
    void editFeed() throws Exception {
        //given
        Post post = Post.createFeed(member, BASIC_FEED_CONTENT);
        PostImage postImage = new PostImage(FEED_IMAGE_PATH1, post);
        post.updateFeedImage(List.of(postImage));

        List<PostImage> newPostImages = List.of(new PostImage(FEED_IMAGE_PATH2, post));

        //when
        post.editFeed(EDIT_FEED_CONTENT, newPostImages);

        //then
        assertThat(post.getContent()).isEqualTo(EDIT_FEED_CONTENT);
        assertThat(post.getImages()).isEqualTo(newPostImages);
    }
}