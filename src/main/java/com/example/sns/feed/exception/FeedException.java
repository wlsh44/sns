package com.example.sns.feed.exception;

import com.example.sns.common.exception.SnsException;
import org.springframework.http.HttpStatus;

public class FeedException extends SnsException {

    public FeedException(String errorMsg, HttpStatus status) {
        super(errorMsg, status);
    }
}
