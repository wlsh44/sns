package com.example.sns.post.domain;

import com.example.sns.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
    private List<Comment> comments = new ArrayList<>();

    public Post(Member member, String content) {
        this.content = content;
        this.member = member;
    }

    public static Post createPost(Member member, String content) {
        return new Post(member, getEmptyStringIfContentNull(content));
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
    }
}
