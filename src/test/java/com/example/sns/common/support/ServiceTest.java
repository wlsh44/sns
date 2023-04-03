package com.example.sns.common.support;

import com.example.sns.auth.application.dto.OAuthUserInfoDto;
import com.example.sns.common.annotation.ApplicationTest;
import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

import static com.example.sns.common.fixtures.MemberFixture.BASIC_EMAIL;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_NAME;
import static com.example.sns.common.fixtures.MemberFixture.BASIC_SOCIAL_ID;

@ApplicationTest
public class ServiceTest {

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected EntityManager em;

    protected Member member;

    @BeforeEach
    void init() {
        member = memberRepository.save(Member.createUserFrom(new OAuthUserInfoDto(BASIC_NAME, BASIC_EMAIL, BASIC_SOCIAL_ID)));
    }
}
