package com.example.sns.social.exception;

import org.springframework.http.HttpStatus;

public class NotFollowingMemberException extends SocialException {

    public static final String ERROR_MSG = "팔로우 하지 않은 유저입니다.";

    public NotFollowingMemberException() {
        super(ERROR_MSG, HttpStatus.BAD_REQUEST);
    }
}
