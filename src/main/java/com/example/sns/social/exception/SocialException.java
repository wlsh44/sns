package com.example.sns.social.exception;

import com.example.sns.common.exception.SnsException;
import org.springframework.http.HttpStatus;

public class SocialException extends SnsException {

    public SocialException(String errorMsg, HttpStatus status) {
        super(errorMsg, status);
    }
}
