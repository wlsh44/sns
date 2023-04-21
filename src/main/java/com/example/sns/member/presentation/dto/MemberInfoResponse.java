package com.example.sns.member.presentation.dto;

import com.example.sns.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberInfoResponse {

    private final Long id;
    private final String username;
    private final String nickname;
    private final String profileImage;
    private final String biography;
    private final String email;

    public static MemberInfoResponse from(Member member) {
        return new MemberInfoResponse(
                member.getId(),
                member.getInfo().getName(),
                member.getInfo().getNickname(),
                member.getProfileUrl(),
                member.getBiography(),
                member.getInfo().getEmail()
        );
    }
}
