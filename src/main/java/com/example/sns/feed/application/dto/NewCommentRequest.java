package com.example.sns.feed.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentRequest {

    private Long feedId;
    private String content;
}
