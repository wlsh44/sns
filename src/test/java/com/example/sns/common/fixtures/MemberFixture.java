package com.example.sns.common.fixtures;

import com.example.sns.auth.application.dto.OAuthUserInfoDto;
import com.example.sns.user.domain.Member;

public class MemberFixture {

    public static final String BASIC_NAME = "name";
    public static final String BASIC_NICKNAME = "nickname";
    public static final String BASIC_EMAIL = "nickname@test.test";
    public static final String BASIC_SOCIAL_ID = "1234567890";

    public static Member getBasicMember() {
        return Member.createUserFrom(new OAuthUserInfoDto(BASIC_NAME, BASIC_EMAIL, BASIC_SOCIAL_ID));
    }
}
