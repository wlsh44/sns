package com.example.sns.post.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p " +
            "join Follow f on f.following.id = p.author.member.id " +
            "where f.follower.id = :memberId order by p.createdAt desc")
    Slice<Post> findMyFeed(@Param("memberId") Long memberId, Pageable pageable);

    @Query("select p from Post p join fetch p.images order by p.createdAt desc")
    Slice<Post> findRecentFeed(Pageable pageable);

    @Query("select p from Post p join fetch p.images i where p.id = :postId")
    Optional<Post> findByIdFetchWithImages(@Param("postId") Long postId);
}
