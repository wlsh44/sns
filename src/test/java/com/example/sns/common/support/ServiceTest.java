package com.example.sns.common.support;

import com.example.sns.common.annotation.ApplicationTest;
import com.example.sns.member.domain.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

@ApplicationTest
public class ServiceTest {

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected EntityManager em;
}
