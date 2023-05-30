package com.example.sns.post.application;

import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import com.example.sns.member.exception.MemberNotFoundException;
import com.example.sns.post.domain.Like;
import com.example.sns.post.domain.LikeRepository;
import com.example.sns.post.domain.Post;
import com.example.sns.post.domain.PostRepository;
import com.example.sns.post.exception.AlreadyLikedPostException;
import com.example.sns.post.exception.NotLikedPostException;
import com.example.sns.post.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    public void like(Long memberId, Long postId) {
        Member member = getMember(memberId);
        Post post = getPost(postId);

        validateAlreadyLikedPost(memberId, postId);

        likeRepository.save(new Like(post, member));
        post.increaseLikeCount();
    }

    private void validateAlreadyLikedPost(Long memberId, Long postId) {
        if (likeRepository.existsByMemberIdAndPostId(memberId, postId)) {
            throw new AlreadyLikedPostException(postId, memberId);
        }
    }

    public void cancelLike(Long memberId, Long postId) {
        validateExistsMember(memberId);
        Post post = getPost(postId);
        Like like = getLike(memberId, postId);

        likeRepository.delete(like);
        post.decreaseLikeCount();
    }

    private void validateExistsMember(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException(memberId);
        }
    }

    private Like getLike(Long memberId, Long postId) {
        return likeRepository.findByMemberIdAndPostId(memberId, postId)
                .orElseThrow(NotLikedPostException::new);
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
    }
}
