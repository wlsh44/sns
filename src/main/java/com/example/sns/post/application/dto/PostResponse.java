package com.example.sns.post.application.dto;

import com.example.sns.member.domain.Member;
import com.example.sns.post.domain.Post;
import com.example.sns.post.domain.PostImage;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostResponse {

    private final Long id;
    private final String nickname;
    private final List<String> images;
    private final int likeCnt;
    private final String content;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate createdAt;

    @JsonProperty(namespace = "isLike")
    private final boolean like;

    public PostResponse(Long id, String nickname, List<String> images, int likeCnt, String content, LocalDate createdAt, boolean like) {
        this.id = id;
        this.nickname = nickname;
        this.images = images;
        this.likeCnt = likeCnt;
        this.content = content;
        this.createdAt = createdAt;
        this.like = like;
    }

    public static PostResponse from(Post post, Member member) {
        Member author = post.getAuthor();
        return new PostResponse(
                post.getId(),
                author.getInfo().getNickname(),
                getImageUrls(post.getImages()),
                post.getLikes().size(),
                post.getContent(),
                post.getCreatedAt().toLocalDate(),
                post.isLikedBy(member)
        );
    }

    private static List<String> getImageUrls(List<PostImage> images) {
        return images.stream()
                .map(PostImage::getImagePath)
                .toList();
    }
}
