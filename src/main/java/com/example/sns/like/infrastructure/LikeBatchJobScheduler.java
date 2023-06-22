package com.example.sns.like.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeBatchJobScheduler {

    private final LikeBatchJobService likeBatchJobService;

    @Scheduled(fixedDelay = 1000 * 60 * 2)
    public void execute() {
        LocalDateTime start = LocalDateTime.now();
        log.info("배치 스케줄링 시작 Time: {}", start);

        executeJob();

        LocalDateTime end = LocalDateTime.now();
        log.info("배치 스케줄링 종료 Time: {}, elapsed: {}", end, Duration.between(start, end));
    }

    private void executeJob() {
        LocalDateTime insertStart = LocalDateTime.now();

        likeBatchJobService.updateRDB();
        log.info("배치 작업 종료 elapsed: {}", Duration.between(insertStart, LocalDateTime.now()));
    }
}
