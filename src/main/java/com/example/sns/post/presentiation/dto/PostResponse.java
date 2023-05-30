package com.example.sns.post.presentiation.dto;

import com.example.sns.post.domain.Author;
import com.example.sns.post.domain.Post;
import com.example.sns.post.domain.PostImage;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class PostResponse {

    private final Long id;
    private final Long authorId;
    private final String authorUsername;
    private final String authorProfile;
    private final List<String> images;
    private final int likeCnt;
    private final String content;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime createdAt;

    @JsonProperty(namespace = "isLike")
    private final boolean like;

    public static PostResponse from(Post post, boolean like) {
        Author author = post.getAuthor();
        return new PostResponse(
                post.getId(),
                author.getId(),
                author.getUsername(),
                author.getProfile(),
                getImageUrls(post.getImages()),
                post.getLikeCount(),
                post.getContent(),
                post.getCreatedAt(),
                like
        );
    }

    private static List<String> getImageUrls(List<PostImage> images) {
        return images.stream()
                .map(PostImage::getImagePath)
                .toList();
    }
}
