package com.example.sns.user.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Getter
@Embeddable
public class Profile {

    private String imagePath;

    private String imageName;
}
