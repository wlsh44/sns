package com.example.sns.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SnsException extends RuntimeException {

    public static final String ERROR_MSG = "예상치 못한 에러입니다.";

    private final String errorMsg;
    private final HttpStatus status;
    private final Throwable throwable;

    public SnsException(String errorMsg, HttpStatus status) {
        this.errorMsg = errorMsg;
        this.status = status;
        this.throwable = null;
    }

    public SnsException(String errorMsg, Throwable throwable) {
        this.errorMsg = errorMsg;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.throwable = throwable;
    }
}
