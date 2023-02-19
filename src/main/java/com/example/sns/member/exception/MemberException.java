package com.example.sns.member.exception;

import com.example.sns.common.exception.SnsException;
import org.springframework.http.HttpStatus;

public class MemberException extends SnsException {

    public static final String ERROR_MSG = "예상치 못한 유저 에러입니다.";

    public MemberException(String errorMsg, HttpStatus status) {
        super(errorMsg, status);
    }

    public MemberException() {
        super(ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
