package com.example.sns.post.presentiation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MyFeedResponse {

    private final List<PostResponse> feed;
    private final boolean last;
    private final int offset;

    public static MyFeedResponse from(List<PostResponse> feed, boolean hasNext, int offset) {
        return new MyFeedResponse(feed, !hasNext, offset);
    }
}
