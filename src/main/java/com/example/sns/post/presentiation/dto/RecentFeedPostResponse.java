package com.example.sns.post.presentiation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RecentFeedPostResponse {
    private final Long id;
    private final String imageUrl;
}
