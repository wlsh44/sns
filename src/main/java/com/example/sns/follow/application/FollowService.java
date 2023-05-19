package com.example.sns.follow.application;

import com.example.sns.follow.domain.Follow;
import com.example.sns.follow.domain.FollowRepository;
import com.example.sns.follow.exception.AlreadyFollowException;
import com.example.sns.follow.exception.NotFollowingMemberException;
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
    private final FollowRepository followRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void follow(Long memberId, Long followingId) {
        Member follower = getMember(memberId);
        Member following = getMember(followingId);

        validateAlreadyFollow(memberId, followingId);
        Follow followTable = Follow.createFollowTable(follower, following);
        follower.follow(followTable);

        eventPublisher.publishEvent(new FollowedEvent(follower, following));
    }

    private void validateAlreadyFollow(Long memberId, Long followingId) {
        if (followRepository.existsByFollowerIdAndFollowingId(memberId, followingId)) {
            throw new AlreadyFollowException();
        }
    }

    @Transactional
    public void unfollow(Long memberId, Long followingId) {
        validateExistsMember(followingId);
        Member follower = getMember(memberId);
        Follow follow = followRepository.findByFollowerIdAndFollowingId(memberId, followingId)
                .orElseThrow(NotFollowingMemberException::new);

        follower.unfollow(follow);
    }

    private void validateExistsMember(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException(memberId);
        }
    }

    private Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
    }
}
