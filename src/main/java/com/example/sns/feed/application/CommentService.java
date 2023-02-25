package com.example.sns.feed.application;

import com.example.sns.feed.application.dto.NewCommentRequest;
import com.example.sns.feed.domain.Comment;
import com.example.sns.feed.domain.CommentRepository;
import com.example.sns.feed.domain.Feed;
import com.example.sns.feed.domain.FeedRepository;
import com.example.sns.feed.exception.FeedNotFoundException;
import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import com.example.sns.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final FeedRepository feedRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void createComment(Long memberId, NewCommentRequest request) {
        Member member = getMember(memberId);
        Feed feed = getFeed(request);

        Comment comment = Comment.createComment(member, feed, request.getContent());
        commentRepository.save(comment);
    }

    private Feed getFeed(NewCommentRequest request) {
        return feedRepository.findById(request.getFeedId())
                .orElseThrow(() -> new FeedNotFoundException(request.getFeedId()));
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
    }
}
