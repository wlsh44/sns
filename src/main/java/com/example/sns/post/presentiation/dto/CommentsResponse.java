package com.example.sns.post.presentiation.dto;

import com.example.sns.post.domain.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CommentsResponse {
    private final List<CommentResponse> comments;
    private final boolean last;
    private final int offset;

    public static CommentsResponse from(List<Comment> comments, boolean hasNext, int offset) {
        List<CommentResponse> commentResponseList = comments.stream()
                .map(CommentResponse::from)
                .toList();
        return new CommentsResponse(commentResponseList, !hasNext, offset);
    }
}
