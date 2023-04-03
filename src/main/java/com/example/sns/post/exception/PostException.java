package com.example.sns.post.exception;

import com.example.sns.common.exception.SnsException;
import org.springframework.http.HttpStatus;

public class PostException extends SnsException {

    public PostException(String errorMsg, HttpStatus status) {
        super(errorMsg, status);
    }
}
