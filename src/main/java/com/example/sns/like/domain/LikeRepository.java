package com.example.sns.like.domain;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository {
    boolean existsByMemberIdAndPostId(Long memberId, Long postId);

    Optional<Like> findByMemberIdAndPostId(Long memberId, Long postId);

    void remove(Like like);

    void save(Like like);
}
