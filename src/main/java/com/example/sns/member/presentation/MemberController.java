package com.example.sns.member.presentation;

import com.example.sns.auth.presentation.Authenticated;
import com.example.sns.member.application.MemberCommandService;
import com.example.sns.member.application.MemberQueryService;
import com.example.sns.member.application.dto.DeviceTokenRequest;
import com.example.sns.member.application.dto.MemberUpdateRequest;
import com.example.sns.member.presentation.dto.MemberInfoResponse;
import com.example.sns.member.presentation.dto.MemberProfileResponse;
import com.example.sns.member.presentation.dto.MemberSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

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
            @RequestPart("body") @Valid MemberUpdateRequest request,
            @RequestPart("image") MultipartFile multipartFile) {
        memberCommandService.updateMember(memberId, request, multipartFile);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<MemberSearchResponse> searchMembers(@RequestParam String username, @PageableDefault Pageable pageable) {
        MemberSearchResponse response = memberQueryService.searchMembers(username, pageable);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/device")
    public ResponseEntity<Void> addDevice(@Authenticated Long memberId, @RequestBody @Valid DeviceTokenRequest request) {
        memberCommandService.addDeviceToken(memberId, request.getToken());

        return ResponseEntity.ok().build();
    }
}
