package com.example.sns.member.presentation.dto;

import com.example.sns.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MemberSearchResponse {

    private final List<MemberSearchDto> members;
    private final boolean last;
    private final int offset;

    public static MemberSearchResponse from(List<Member> members, boolean hasNext, int offset) {
        return new MemberSearchResponse(getMemberSearchDtoList(members), !hasNext, offset);
    }

    private static List<MemberSearchDto> getMemberSearchDtoList(List<Member> members) {
        return members.stream()
                .map(member -> new MemberSearchDto(member.getId(), member.getInfo().getNickname(), member.getProfileUrl()))
                .toList();
    }
}
