package com.example.sns.alarm.application;

import java.util.List;

public interface AlarmService {
    void send(String token);
    void sendAll(List<String> tokens);
}
