package com.example.sns.post.presentiation.dto;

import com.example.sns.post.domain.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CommentResponse {

    private final Long id;
    private final Long authorId;
    private final String authorProfile;
    private final String authorNickname;
    private final String content;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime createdAt;

    public CommentResponse(Comment comment) {
        this(
                comment.getId(),
                comment.getAuthor().getId(),
                comment.getAuthor().getProfile(),
                comment.getAuthor().getNickname(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}
