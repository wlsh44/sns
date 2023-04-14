package com.example.sns.post.application;

import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import com.example.sns.member.exception.MemberNotFoundException;
import com.example.sns.post.domain.Feed;
import com.example.sns.post.domain.PostRepository;
import com.example.sns.post.presentiation.dto.MyFeedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public MyFeedResponse findMyFeed(Long memberId, Pageable pageable) {
        Member member = getMember(memberId);
        Feed feed = new Feed(postRepository.findMyFeed(memberId, pageable));

        return MyFeedResponse.from(feed, member);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
    }
}
