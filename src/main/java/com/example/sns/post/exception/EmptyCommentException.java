package com.example.sns.post.exception;

import org.springframework.http.HttpStatus;

public class EmptyCommentException extends CommentException {

    public static final String ERROR_MSG = "댓글의 내용이 없습니다.";

    public EmptyCommentException() {
        super(ERROR_MSG, HttpStatus.BAD_REQUEST);
    }
}
