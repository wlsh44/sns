package com.example.sns.post.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByMemberIdAndPostId(Long memberId, Long postId);

    Optional<Like> findByMemberIdAndPostId(Long memberId, Long postId);
}
