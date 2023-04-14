package com.example.sns.post.presentiation.dto;

import com.example.sns.member.domain.Member;
import com.example.sns.post.application.dto.PostResponse;
import com.example.sns.post.domain.Feed;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MyFeedResponse {

    private final List<PostResponse> feed;

    public static MyFeedResponse from(Feed feed, Member member) {
        List<PostResponse> postResponseList = feed.getPosts().stream()
                .map(post -> PostResponse.from(post, member))
                .toList();
        return new MyFeedResponse(postResponseList);
    }
}
