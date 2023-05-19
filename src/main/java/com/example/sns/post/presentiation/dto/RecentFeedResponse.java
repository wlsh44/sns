package com.example.sns.post.presentiation.dto;

import com.example.sns.post.domain.Post;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class RecentFeedResponse {

    private final List<RecentFeedPostResponse> timeline;
    private final boolean last;
    private final int offset;

    public static RecentFeedResponse from(List<Post> posts, boolean hasNext, int offset) {
        List<RecentFeedPostResponse> timeline = posts.stream()
                .map(post -> new RecentFeedPostResponse(post.getId(), post.getThumbnailImagePath()))
                .toList();
        return new RecentFeedResponse(timeline, !hasNext, offset);
    }
}
