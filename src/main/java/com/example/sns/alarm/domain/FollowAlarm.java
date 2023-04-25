package com.example.sns.alarm.domain;

import com.example.sns.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowAlarm extends Alarm {

    @ManyToOne
    private Member following;

    public FollowAlarm(Member target, Member following) {
        super(target, AlarmText.FOLLOW.getText(following.getInfo().getNickname()));
        this.following = following;
    }
}
