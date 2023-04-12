package com.example.sns.follow.application;

import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import com.example.sns.member.exception.MemberNotFoundException;
import com.example.sns.follow.application.dto.FollowRequest;
import com.example.sns.follow.application.dto.UnfollowRequest;
import com.example.sns.follow.domain.Follow;
import com.example.sns.follow.domain.FollowRepository;
import com.example.sns.follow.exception.AlreadyFollowException;
import com.example.sns.follow.exception.NotFollowingMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void follow(Long memberId, FollowRequest request) {
        Member follower = getMember(memberId);
        Member following = getMember(request.getFollowingId());

        validateAlreadyFollow(follower, following);

        Follow followTable = follower.follow(following);
        followRepository.save(followTable);
    }

    private void validateAlreadyFollow(Member follower, Member following) {
        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new AlreadyFollowException();
        }
    }

    private Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
    }

    @Transactional
    public void unfollow(Long memberId, UnfollowRequest request) {
        Member follower = getMember(memberId);
        Member following = getMember(request.getFollowingId());

        Follow followTable = followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(NotFollowingMemberException::new);

        follower.unfollow(followTable);
        followRepository.delete(followTable);
    }
}
