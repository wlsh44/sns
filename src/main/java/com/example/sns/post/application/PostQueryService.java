package com.example.sns.post.application;

import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import com.example.sns.member.exception.MemberNotFoundException;
import com.example.sns.post.domain.LikeRepository;
import com.example.sns.post.presentiation.dto.PostResponse;
import com.example.sns.post.domain.Post;
import com.example.sns.post.domain.PostRepository;
import com.example.sns.post.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    public PostResponse findPost(Long memberId, Long postId) {
        Member member = getMember(memberId);
        Post post = getPost(postId);

        boolean like = likeRepository.existsByMemberIdAndPostId(memberId, postId);
        return PostResponse.from(post, member, like);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
    }

    private Post getPost(Long postId) {
        return postRepository.findByIdFetchWithImages(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
    }
}
