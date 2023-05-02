package com.example.sns.alarm.domain;

import com.example.sns.common.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.List;

import static com.example.sns.alarm.domain.AlarmType.FOLLOW;
import static com.example.sns.alarm.domain.AlarmType.POST_UPLOAD;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long targetId;

    private boolean read;

    private String text;

    private AlarmType type;

    private Alarm(Long targetId, String text, AlarmType type) {
        this.targetId = targetId;
        this.text = text;
        this.type = type;
        this.read = false;
    }

    public static Alarm createFollowedAlarm(Long targetId, String text) {
        return new Alarm(targetId, text, FOLLOW);
    }

    public static List<Alarm> createPostUploadedAlarms(List<Long> targetIds, String text) {
        return targetIds.stream()
                .map(targetId -> new Alarm(targetId, text, POST_UPLOAD))
                .toList();
    }
}
