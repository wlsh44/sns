package com.example.sns.like.application;

import com.example.sns.like.domain.Like;
import com.example.sns.like.domain.LikeRepository;
import com.example.sns.post.exception.AlreadyLikedPostException;
import com.example.sns.post.exception.NotLikedPostException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    public void like(Long memberId, Long postId) {
        validateAlreadyLikedPost(memberId, postId);

        likeRepository.save(new Like(memberId, postId));
    }

    private void validateAlreadyLikedPost(Long memberId, Long postId) {
        if (likeRepository.existsByMemberIdAndPostId(memberId, postId)) {
            throw new AlreadyLikedPostException(postId, memberId);
        }
    }

    public void cancelLike(Long memberId, Long postId) {
        validateLikedPost(memberId, postId);
        likeRepository.removeByMemberIdAndPostId(memberId, postId);
    }

    private void validateLikedPost(Long memberId, Long postId) {
        if (!likeRepository.existsByMemberIdAndPostId(memberId, postId)) {
            throw new NotLikedPostException();
        }
    }
}
