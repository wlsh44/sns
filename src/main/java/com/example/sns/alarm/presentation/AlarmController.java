package com.example.sns.alarm.presentation;

import com.example.sns.alarm.application.AlarmCommandService;
import com.example.sns.alarm.application.AlarmQueryService;
import com.example.sns.alarm.presentation.dto.AlarmListResponse;
import com.example.sns.auth.presentation.Authenticated;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alarms")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmQueryService alarmQueryService;
    private final AlarmCommandService alarmCommandService;

    @GetMapping("")
    public ResponseEntity<AlarmListResponse> findAlarms(@Authenticated Long memberId, @PageableDefault Pageable pageable) {
        AlarmListResponse response = alarmQueryService.findAlarms(memberId, pageable);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{alarmId}/read")
    public ResponseEntity<Void> readAlarm(@Authenticated Long memberId, @PathVariable Long alarmId) {
        alarmCommandService.readAlarm(memberId, alarmId);

        return ResponseEntity.ok().build();
    }
}
