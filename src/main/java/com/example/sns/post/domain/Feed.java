package com.example.sns.post.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class Feed {

    private final List<Post> posts;
}
