package com.example.sns.alarm.domain;

public enum AlarmType {
    FOLLOW("%s님이 회원님을 팔로우하기 시작했습니다."),
    POST_UPLOAD("%s님이 게시글을 작성했습니다.");

    private final String text;

    AlarmType(String text) {
        this.text = text;
    }

    public String getText(String param) {
        return String.format(text, param);
    }
}
