package com.example.sns.post.application;

import com.example.sns.common.infrastructure.fcm.dto.AlarmTargetsDto;
import com.example.sns.member.domain.Member;
import lombok.Getter;

import java.util.List;

@Getter
public class PostUploadedEvent {

    private final AlarmTargetsDto targets;
    private final String authorUsername;

    public PostUploadedEvent(List<Member> targets, Member postAuthor) {
        this.authorUsername = postAuthor.getInfo().getUsername();
        this.targets = new AlarmTargetsDto(targets);
    }
}
