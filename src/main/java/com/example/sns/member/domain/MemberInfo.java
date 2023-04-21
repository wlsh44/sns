package com.example.sns.member.domain;

import com.example.sns.member.exception.InvalidNicknameException;
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

    private String nickname;

    private String email;

    public void updateNickname(String nickname) {
        validateNickname(nickname);
        this.nickname = nickname;
    }

    private void validateNickname(String nickname) {
        if (!StringUtils.hasText(nickname)) {
            throw new InvalidNicknameException(nickname);
        }
    }
}
