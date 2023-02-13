package com.example.sns.user.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Getter
@Embeddable
public class UserInfo {

    private String userName;

    private String nickName;

    private String email;
}
