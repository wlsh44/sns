package com.example.sns.post.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Slice<Comment> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);
}
