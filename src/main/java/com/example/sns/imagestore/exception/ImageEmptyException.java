package com.example.sns.imagestore.exception;

import org.springframework.http.HttpStatus;

public class ImageEmptyException extends ImageStoreException {

    public static final String ERROR_MSG = "올바른 이미지 형식이 아닙니다.";

    public ImageEmptyException() {
        super(ERROR_MSG, HttpStatus.BAD_REQUEST);
    }
}
