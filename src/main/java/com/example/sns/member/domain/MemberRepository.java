package com.example.sns.member.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySocialId(String socialId);
    Optional<Member> findByInfoUsername(String username);
    boolean existsByInfoUsername(String username);
    Slice<Member> findByInfoUsernameContaining(String username, Pageable pageable);
}
