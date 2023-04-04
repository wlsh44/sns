package com.example.sns.post.exception;

import org.springframework.http.HttpStatus;

public class NotCommentAuthorException extends CommentException {

    public static final String ERROR_MSG = "댓글 작성자가 아닙니다.";

    public NotCommentAuthorException() {
        super(ERROR_MSG, HttpStatus.BAD_REQUEST);
    }
}
