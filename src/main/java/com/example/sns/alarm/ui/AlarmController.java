package com.example.sns.alarm.ui;

import com.example.sns.alarm.application.AlarmQueryService;
import com.example.sns.alarm.ui.dto.AlarmListResponse;
import com.example.sns.auth.presentation.Authenticated;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alarms")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmQueryService alarmQueryService;

    @GetMapping("")
    public ResponseEntity<AlarmListResponse> findAlarms(@Authenticated Long memberId, @PageableDefault Pageable pageable) {
        AlarmListResponse response = alarmQueryService.findAlarms(memberId, pageable);

        return ResponseEntity.ok(response);
    }
}
