package com.example.sns.post.application;

import com.example.sns.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class PostUploadedEvent {

    private final List<Member> targets;
    private final Member postAuthor;
}
