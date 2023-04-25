package com.example.sns.alarm.domain;

public enum AlarmText {
    FOLLOW("%s님이 회원님을 팔로우하기 시작했습니다.");

    private final String text;

    AlarmText(String text) {
        this.text = text;
    }

    public String getText(String nickname) {
        return String.format(text, nickname);
    }
}
