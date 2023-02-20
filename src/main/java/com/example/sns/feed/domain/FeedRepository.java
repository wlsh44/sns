package com.example.sns.feed.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    Optional<Feed> findByIdAndMemberId(Long id, Long emberId);
}
