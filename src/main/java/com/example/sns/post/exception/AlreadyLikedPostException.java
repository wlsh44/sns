package com.example.sns.post.exception;

import com.example.sns.common.exception.SnsException;
import org.springframework.http.HttpStatus;

public class AlreadyLikedPostException extends SnsException {

    public static final String ERROR_MSG = "이미 좋아요를 누른 게시글입니다. post ID: %d, member ID: %d";

    public AlreadyLikedPostException(Long postId, Long memberId) {
        super(String.format(ERROR_MSG, postId, memberId), HttpStatus.BAD_REQUEST);
    }
}
