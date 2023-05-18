package com.example.sns.common.infrastructure.fcm.dto;

import com.example.sns.member.domain.Member;

import java.util.Collection;
import java.util.List;

public class AlarmTargetsDto {

    private final List<AlarmTargetDto> targets;

    public AlarmTargetsDto(List<Member> targets) {
        this.targets = targets.stream()
                .map(target -> new AlarmTargetDto(target.getId(), target.getDeviceTokens()))
                .toList();
    }

    public List<Long> getTargetIds() {
        return targets.stream()
                .map(AlarmTargetDto::getTargetId)
                .toList();
    }

    public List<String> getTargetDeviceTokens() {
        return targets.stream()
                .map(AlarmTargetDto::getTargetDeviceToken)
                .flatMap(Collection::stream)
                .toList();
    }
}
