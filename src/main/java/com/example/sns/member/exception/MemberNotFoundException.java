package com.example.sns.member.exception;

import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends MemberException {
    public static final String ERROR_MSG = "해당 유저가 존재하지 않습니다. id = %d";

    public MemberNotFoundException(Long id) {
        super(String.format(ERROR_MSG, id), HttpStatus.BAD_REQUEST);
    }
}