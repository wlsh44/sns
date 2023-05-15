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
public class MemberInfo {

    private String name;

    private String username;

    private String email;

    public void updateUsername(String username) {
        validateUsername(username);
        this.username = username;
    }

    private void validateUsername(String username) {
        if (!StringUtils.hasText(username)) {
            throw new InvalidUsernameException(username);
        }
    }
}
