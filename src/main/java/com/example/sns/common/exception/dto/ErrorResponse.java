package com.example.sns.common.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private final String errorMsg;

    public ErrorResponse() {
        this.errorMsg = null;
    }
}
