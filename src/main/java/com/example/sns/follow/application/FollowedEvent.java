package com.example.sns.follow.application;

import com.example.sns.common.infrastructure.fcm.dto.AlarmTargetDto;
import com.example.sns.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class FollowedEvent {
    private final AlarmTargetDto target;
    private final String followerNickname;

    public FollowedEvent(Member follower, Member following) {
        this.target = new AlarmTargetDto(following.getId(), following.getDeviceTokens());
        this.followerNickname = follower.getInfo().getNickname();
    }
}
