package com.example.sns.member.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySocialId(String socialId);
    Optional<Member> findBySocialInfoUsername(String username);
    boolean existsBySocialInfoUsername(String username);
    Slice<Member> findBySocialInfoUsernameContaining(String username, Pageable pageable);
}
