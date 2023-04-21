package com.example.sns.member.presentation;

import com.example.sns.auth.presentation.Authenticated;
import com.example.sns.member.application.MemberCommandService;
import com.example.sns.member.application.MemberQueryService;
import com.example.sns.member.application.dto.MemberUpdateRequest;
import com.example.sns.member.presentation.dto.MemberInfoResponse;
import com.example.sns.member.presentation.dto.MemberProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    @GetMapping("/profile")
    public ResponseEntity<MemberProfileResponse> getProfile(@Authenticated Long memberId, @RequestParam String username) {
        MemberProfileResponse response = memberQueryService.getProfile(memberId, username);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<MemberInfoResponse> getMemberInfo(@Authenticated Long memberId) {
        MemberInfoResponse response = memberQueryService.getMemberInfo(memberId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/me")
    public ResponseEntity<Void> updateMember(
            @Authenticated Long memberId,
            @RequestPart("body") MemberUpdateRequest request,
            @RequestPart("image") MultipartFile multipartFile) {
        memberCommandService.updateMember(memberId, request, multipartFile);

        return ResponseEntity.ok().build();
    }
}
