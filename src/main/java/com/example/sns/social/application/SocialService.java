package com.example.sns.social.application;

import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import com.example.sns.member.exception.MemberNotFoundException;
import com.example.sns.social.application.dto.FollowRequest;
import com.example.sns.social.domain.Follow;
import com.example.sns.social.domain.FollowRepository;
import com.example.sns.social.exception.AlreadyFollowException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SocialService {

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
}
