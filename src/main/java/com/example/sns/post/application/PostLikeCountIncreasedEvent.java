package com.example.sns.post.application;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostLikeCountIncreasedEvent {
    private final Long postId;
}
