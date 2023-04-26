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

import static com.example.sns.alarm.domain.AlarmType.FOLLOW;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member target;

    private boolean read;

    private String text;

    private AlarmType type;

    private Alarm(Member target, String text, AlarmType type) {
        this.target = target;
        this.text = text;
        this.type = type;
        this.read = false;
    }

    public static Alarm createFollowedAlarm(Member target, Member follower) {
        String text = FOLLOW.getText(follower.getInfo().getNickname());
        return new Alarm(target, text, FOLLOW);
    }
}
