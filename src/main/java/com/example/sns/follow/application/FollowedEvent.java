package com.example.sns.follow.application;

import com.example.sns.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FollowedEvent {
    private final Member follower;
    private final Member following;
}
