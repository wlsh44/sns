package com.example.sns.follow.exception;

import com.example.sns.common.exception.SnsException;
import org.springframework.http.HttpStatus;

public class NotFollowingMemberException extends SnsException {

    public static final String ERROR_MSG = "팔로우 하지 않은 유저입니다.";

    public NotFollowingMemberException() {
        super(ERROR_MSG, HttpStatus.BAD_REQUEST);
    }
}
