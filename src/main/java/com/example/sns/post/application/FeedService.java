package com.example.sns.post.application;

import com.example.sns.like.domain.LikeRepository;
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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    public MyFeedResponse findMyFeed(Long memberId, Pageable pageable) {
        Slice<Post> myFeedSlice = postRepository.findMyFeed(memberId, pageable);

        List<PostResponse> postResponses = getPostResponses(memberId, myFeedSlice);
        return MyFeedResponse.from(postResponses, myFeedSlice.hasNext(), myFeedSlice.getNumber());
    }

    private List<PostResponse> getPostResponses(Long memberId, Slice<Post> myFeedSlice) {
        return myFeedSlice.getContent().stream()
                .map(post -> PostResponse.from(post, isMemberLikePost(memberId, post), likeRepository.countByPostId(post.getId())))
                .toList();
    }

    private boolean isMemberLikePost(Long memberId, Post post) {
        return likeRepository.existsByMemberIdAndPostId(memberId, post.getId());
    }

    public RecentFeedResponse findRecentFeed(Pageable pageable) {
        Slice<Post> recentFeedSlice = postRepository.findRecentFeed(pageable);

        return RecentFeedResponse.from(recentFeedSlice.getContent(), recentFeedSlice.hasNext(), recentFeedSlice.getNumber());
    }
}
