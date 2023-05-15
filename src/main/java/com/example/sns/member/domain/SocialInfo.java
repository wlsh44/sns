package com.example.sns.member.domain;

import com.example.sns.member.exception.InvalidUsernameException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialInfo {

    private String username;

    private String profileUrl;

    private String biography;

    public SocialInfo(String username) {
        this.username = username;
    }

    public void update(String username, String biography, String profilePath) {
        validateUsername(username);
        this.username = username;
        this.biography = biography;
        this.profileUrl = profilePath;
    }

    private void validateUsername(String username) {
        if (!StringUtils.hasText(username)) {
            throw new InvalidUsernameException(username);
        }
    }
}
