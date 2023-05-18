package com.example.sns.member.presentation.dto;

import com.example.sns.member.domain.DetailedInfo;
import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.SocialInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberInfoResponse {

    private final Long id;
    private final String name;
    private final String username;
    private final String profileImage;
    private final String biography;
    private final String email;

    public static MemberInfoResponse from(Member member) {
        DetailedInfo detailedInfo = member.getDetailedInfo();
        SocialInfo socialInfo = member.getSocialInfo();
        return new MemberInfoResponse(
                member.getId(),
                detailedInfo.getName(),
                socialInfo.getUsername(),
                socialInfo.getProfileUrl(),
                socialInfo.getBiography(),
                detailedInfo.getEmail()
        );
    }
}
