package com.example.sns.member.application;

import com.example.sns.member.application.dto.MemberUpdateRequest;
import com.example.sns.member.domain.Member;
import com.example.sns.member.domain.MemberRepository;
import com.example.sns.member.exception.AlreadyExistUsernameException;
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

        validateExistUsername(request);

        String profilePath = imageStore.saveProfileImage(profileImage);
        member.update(request.getUsername(), request.getBiography(), profilePath);
    }

    private void validateExistUsername(MemberUpdateRequest request) {
        if (memberRepository.existsByInfoUsername(request.getUsername())) {
            throw new AlreadyExistUsernameException(request.getUsername());
        }
    }

    public void addDeviceToken(Long memberId, String token) {
        Member member = getMember(memberId);

        member.addDevice(token);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
    }
}
