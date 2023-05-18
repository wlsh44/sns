package com.example.sns.member.application;

import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import com.example.sns.member.exception.MemberNotFoundException;
import com.example.sns.member.presentation.dto.MemberInfoResponse;
import com.example.sns.member.presentation.dto.MemberProfileResponse;
import com.example.sns.member.presentation.dto.MemberSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberRepository memberRepository;

    public MemberProfileResponse getProfile(Long memberId, String username) {
        Member loginMember = getMember(memberId);
        Member profileMember = memberRepository.findBySocialInfoUsername(username)
                .orElseThrow(() -> new MemberNotFoundException(username));

        return MemberProfileResponse.from(profileMember, profileMember.isFollower(loginMember));
    }

    public MemberInfoResponse getMemberInfo(Long memberId) {
        Member member = getMember(memberId);

        return MemberInfoResponse.from(member);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
    }

    public MemberSearchResponse searchMembers(String username, Pageable pageable) {
        Slice<Member> memberSlice = memberRepository.findBySocialInfoUsernameContaining(username, pageable);

        return MemberSearchResponse.from(memberSlice.getContent(), memberSlice.hasNext(), memberSlice.getNumber());
    }
}
