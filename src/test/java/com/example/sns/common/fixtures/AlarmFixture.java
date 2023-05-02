package com.example.sns.common.fixtures;

import com.example.sns.alarm.domain.Alarm;
import com.example.sns.member.domain.Member;

import java.util.List;

import static com.example.sns.alarm.domain.AlarmType.FOLLOW;
import static com.example.sns.alarm.domain.AlarmType.POST_UPLOAD;

public class AlarmFixture {

    public static Alarm getFollowAlarm(Member member) {
        return Alarm.createFollowedAlarm(member.getId(), FOLLOW.getText("following"));
    }

    public static List<Alarm> getPostUploadAlarms(Member member) {
        return Alarm.createPostUploadedAlarms(List.of(member.getId()), POST_UPLOAD.getText("following"));
    }
}
