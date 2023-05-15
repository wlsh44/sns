package com.example.sns.member.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySocialId(String socialId);
    Optional<Member> findByInfoUsername(String username);
    boolean existsByInfoUsername(String username);
    Slice<Member> findByInfoUsernameContaining(String username, Pageable pageable);

    @Query("select m from Member m left join fetch m.devices where m.id = :memberId")
    Optional<Member> findByIdFetchDevices(@Param("memberId") Long memberId);
}
