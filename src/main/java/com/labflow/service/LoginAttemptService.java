package com.labflow.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.time.LocalDateTime;

@Service
public class LoginAttemptService {
    private final int MAX_ATTEMPTS = 3;
    private final int BAN_MINUTES = 5;

    private static class AttemptInfo {
        int attempts;
        LocalDateTime banTime;
        AttemptInfo(int attempts, LocalDateTime banTime) {
            this.attempts = attempts;
            this.banTime = banTime;
        }
    }

    private final Map<String, AttemptInfo> attemptsCache = new ConcurrentHashMap<>();

    public void loginSucceeded(String key) {
        attemptsCache.remove(key);
    }

    public void loginFailed(String key) {
        AttemptInfo info = attemptsCache.getOrDefault(key, new AttemptInfo(0, null));
        if (info.banTime != null && LocalDateTime.now().isBefore(info.banTime)) {
            return;
        }
        
        info.attempts++;
        if (info.attempts >= MAX_ATTEMPTS) {
            info.banTime = LocalDateTime.now().plusMinutes(BAN_MINUTES);
        }
        attemptsCache.put(key, info);
    }

    public boolean isBlocked(String key) {
        AttemptInfo info = attemptsCache.get(key);
        if (info == null) {
            return false;
        }
        if (info.banTime != null && LocalDateTime.now().isBefore(info.banTime)) {
            return true;
        }
        if (info.banTime != null && LocalDateTime.now().isAfter(info.banTime)) {
            attemptsCache.remove(key);
            return false;
        }
        return false;
    }

    public long getRemainingBanTimeMinutes(String key) {
        AttemptInfo info = attemptsCache.get(key);
        if (info != null && info.banTime != null) {
            long minutes = java.time.temporal.ChronoUnit.MINUTES.between(LocalDateTime.now(), info.banTime);
            return minutes < 1 ? 1 : minutes; // If less than 1 min, show 1
        }
        return 0;
    }
}
