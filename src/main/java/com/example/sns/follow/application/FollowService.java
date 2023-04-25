package com.example.sns.follow.application;

import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import com.example.sns.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowService {

    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void follow(Long memberId, Long followingId) {
        Member follower = getMember(memberId);
        Member following = getMember(followingId);

        follower.follow(following);
        eventPublisher.publishEvent(new FollowedEvent(follower, following));
    }

    private Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
    }

    @Transactional
    public void unfollow(Long memberId, Long followingId) {
        Member follower = getMember(memberId);
        Member following = getMember(followingId);

        follower.unfollow(following);
    }
}
