package com.example.sns.member.application;

import com.example.sns.member.application.dto.MemberUpdateRequest;
import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import com.example.sns.member.exception.AlreadyExistNicknameException;
import com.example.sns.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandService {

    private final MemberRepository memberRepository;
    private final ProfileImageStore imageStore;

    public void updateMember(Long memberId, MemberUpdateRequest request, MultipartFile profileImage) {
        Member member = getMember(memberId);

        validateExistNickname(request);

        String profilePath = imageStore.saveProfileImage(profileImage);
        member.update(request.getNickname(), request.getBiography(), profilePath);
    }

    private void validateExistNickname(MemberUpdateRequest request) {
        if (memberRepository.existsByInfoNickname(request.getNickname())) {
            throw new AlreadyExistNicknameException(request.getNickname());
        }
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
    }
}
