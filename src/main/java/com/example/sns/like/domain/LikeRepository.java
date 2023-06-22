package com.example.sns.like.domain;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository {
    boolean existsByMemberIdAndPostId(Long memberId, Long postId);

    void removeByMemberIdAndPostId(Long memberId, Long postId);

    void save(Like like);

    int countByPostId(Long postId);
}
