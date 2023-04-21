package com.example.sns.member.application;

import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import com.example.sns.member.exception.MemberNotFoundException;
import com.example.sns.member.presentation.dto.MemberProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberRepository memberRepository;

    public MemberProfileResponse getProfile(Long memberId, String username) {
        Member loginMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
        Member profileMember = memberRepository.findByInfoNickname(username)
                .orElseThrow(() -> new MemberNotFoundException(username));

        return MemberProfileResponse.from(profileMember, profileMember.isFollower(loginMember));
    }
}
