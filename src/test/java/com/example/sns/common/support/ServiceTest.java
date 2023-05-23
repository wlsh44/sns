package com.example.sns.common.support;

import com.example.sns.common.annotation.ApplicationTest;
import com.example.sns.member.domain.MemberRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityManager;

@ApplicationTest
public abstract class ServiceTest {

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected EntityManager em;

    @MockBean
    FirebaseMessaging firebaseMessaging;
}
