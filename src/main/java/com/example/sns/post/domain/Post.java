package com.example.sns.post.domain;

import com.example.sns.common.entity.BaseTimeEntity;
import com.example.sns.member.domain.Member;
import com.example.sns.post.exception.NotPostAuthorException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Embedded
    private Author author;

    private int likeCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<PostImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
    private final List<Comment> comments = new ArrayList<>();

    public Post(Author author, String content) {
        this.content = content;
        this.author = author;
    }

    public static Post createPost(Member member, String content) {
        return new Post(new Author(member), getEmptyStringIfContentNull(content));
    }

    public void updatePostImage(List<PostImage> postImages) {
        images.clear();
        images.addAll(postImages);
    }

    public void addPostImage(PostImage postImage) {
        images.add(postImage);
        postImage.mappingPost(this);
    }

    public void editPost(String content, List<PostImage> postImages) {
        this.content = getEmptyStringIfContentNull(content);
        updatePostImage(postImages);
    }

    public void deletePost() {
        images.clear();
        comments.clear();
    }

    private static String getEmptyStringIfContentNull(String content) {
        return Optional.ofNullable(content).orElse("");
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.mappingPost(this);
    }

    public void validateIsAuthor(Long memberId) {
        if (!author.isAuthor(memberId)) {
            throw new NotPostAuthorException();
        }
    }

    public String getThumbnailImagePath() {
        return images.get(0).getImagePath();
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void decreaseLikeCount() {
        assert likeCount != 0;
        likeCount--;
    }
}
