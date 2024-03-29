package com.example.sns.member.presentation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberSearchDto {

    private final Long id;
    private final String username;
    private final String profileImage;
}
