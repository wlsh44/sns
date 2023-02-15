package com.example.sns.auth.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class OAuthUserInfoDto {

    private String name;
    private String email;

    @JsonProperty("picture")
    private String imageUrl;

    @JsonProperty("sub")
    private String socialId;
}
