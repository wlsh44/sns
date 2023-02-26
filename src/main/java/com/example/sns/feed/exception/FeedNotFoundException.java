package com.example.sns.feed.exception;

import org.springframework.http.HttpStatus;

public class FeedNotFoundException extends FeedException {

    public static final String ERROR_MSG = "피드가 존재하지 않습니다. id = %d";

    public FeedNotFoundException(Long feedId) {
        super(String.format(ERROR_MSG, feedId), HttpStatus.BAD_REQUEST);
    }
}
