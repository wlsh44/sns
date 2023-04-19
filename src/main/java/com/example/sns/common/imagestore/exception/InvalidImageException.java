package com.example.sns.common.imagestore.exception;

import com.example.sns.common.exception.SnsException;
import org.springframework.http.HttpStatus;

public class InvalidImageException extends SnsException {

    public static final String ERROR_MSG = "올바른 이미지 형식이 아닙니다.";

    public InvalidImageException() {
        super(ERROR_MSG, HttpStatus.BAD_REQUEST);
    }
}
