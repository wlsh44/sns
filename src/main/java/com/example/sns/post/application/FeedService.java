package com.example.sns.post.application;

import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import com.example.sns.member.exception.MemberNotFoundException;
import com.example.sns.post.domain.Feed;
import com.example.sns.post.domain.LikeRepository;
import com.example.sns.post.domain.Post;
import com.example.sns.post.domain.PostRepository;
import com.example.sns.post.presentiation.dto.MyFeedResponse;
import com.example.sns.post.presentiation.dto.PostResponse;
import com.example.sns.post.presentiation.dto.RecentFeedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    public MyFeedResponse findMyFeed(Long memberId, Pageable pageable) {
        Member member = getMember(memberId);
        Slice<Post> myFeedSlice = postRepository.findMyFeed(memberId, pageable);

        List<PostResponse> postResponses = getPostResponses(memberId, member, myFeedSlice);
        return MyFeedResponse.from(postResponses, myFeedSlice.hasNext(), myFeedSlice.getNumber());
    }

    private List<PostResponse> getPostResponses(Long memberId, Member member, Slice<Post> myFeedSlice) {
        return myFeedSlice.getContent().stream()
                .map(post -> PostResponse.from(post, member, likeRepository.existsByMemberIdAndPostId(memberId, post.getId())))
                .collect(Collectors.toList());
    }

    public RecentFeedResponse findRecentFeed(Pageable pageable) {
        Slice<Post> recentFeedSlice = postRepository.findRecentFeed(pageable);

        return RecentFeedResponse.from(recentFeedSlice.getContent(), recentFeedSlice.hasNext(), recentFeedSlice.getNumber());
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
    }
}
