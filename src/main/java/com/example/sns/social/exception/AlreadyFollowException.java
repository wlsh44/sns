package com.example.sns.social.exception;

import org.springframework.http.HttpStatus;

public class AlreadyFollowException extends SocialException {

    public static final String ERROR_MSG = "이미 팔로우 한 유저입니다";

    public AlreadyFollowException() {
        super(ERROR_MSG, HttpStatus.BAD_REQUEST);
    }
}
