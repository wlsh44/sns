package com.example.sns.alarm.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowAlarmRepository extends JpaRepository<FollowAlarm, Long> {
}
