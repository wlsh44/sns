package com.example.sns.member.presentation;

import com.example.sns.auth.presentation.Authenticated;
import com.example.sns.member.application.MemberQueryService;
import com.example.sns.member.presentation.dto.MemberInfoResponse;
import com.example.sns.member.presentation.dto.MemberProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberQueryService memberQueryService;

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
}
