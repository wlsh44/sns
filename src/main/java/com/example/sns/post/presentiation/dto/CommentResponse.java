package com.example.sns.post.presentiation.dto;

import com.example.sns.post.domain.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {

    private final Long id;
    private final Long authorId;
    private final String authorProfile;
    private final String authorUsername;
    private final String content;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime createdAt;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.authorId = comment.getAuthor().getId();
        this.authorProfile = comment.getAuthor().getProfile();
        this.authorUsername = comment.getAuthor().getUsername();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }
}
