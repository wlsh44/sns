package com.example.sns.user.infrastructure;

import com.example.sns.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySocialId(String socialId);
}
