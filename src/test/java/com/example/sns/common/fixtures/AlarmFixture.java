package com.example.sns.common.fixtures;

import com.example.sns.alarm.domain.Alarm;
import com.example.sns.member.domain.Member;


import static com.example.sns.alarm.domain.AlarmType.FOLLOW;

public class AlarmFixture {

    public static Alarm getFollowAlarm(Member member) {
        return Alarm.createFollowedAlarm(member.getId(), FOLLOW.getText("following"));
    }
}
