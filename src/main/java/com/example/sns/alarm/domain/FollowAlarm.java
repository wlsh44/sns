package com.example.sns.alarm.domain;

import com.example.sns.common.entity.BaseTimeEntity;
import com.example.sns.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowAlarm extends Alarm {

    @ManyToOne
    private Member following;

    public FollowAlarm(Member target, Member following) {
        super(target);
        this.following = following;
    }
}
