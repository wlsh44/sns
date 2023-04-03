package com.example.sns.post.exception;

import com.example.sns.common.exception.SnsException;
import org.springframework.http.HttpStatus;

public class CommentException extends SnsException {
    public CommentException(String errorMsg, HttpStatus status) {
        super(errorMsg, status);
    }
}
