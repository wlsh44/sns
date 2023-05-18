package com.example.sns.member.domain;

import com.example.sns.member.exception.InvalidUsernameException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.Embeddable;
import java.util.Optional;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialInfo {

    private static final String DEFAULT_PROFILE = "https://kr.object.ncloudstorage.com/sns-image-s3/profile/default.jpeg";

    private String username;

    private String profileUrl;

    private String biography;

    public SocialInfo(String username) {
        this.username = username;
        this.biography = "";
        this.profileUrl = DEFAULT_PROFILE;
    }

    public void update(String username, String biography, String profilePath) {
        validateUsername(username);
        this.username = username;
        this.biography = Optional.ofNullable(biography).orElse("");
        this.profileUrl = Optional.ofNullable(profilePath).orElse(DEFAULT_PROFILE);
    }

    private void validateUsername(String username) {
        if (!StringUtils.hasText(username)) {
            throw new InvalidUsernameException(username);
        }
    }
}
