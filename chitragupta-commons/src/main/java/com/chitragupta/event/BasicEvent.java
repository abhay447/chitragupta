package com.chitragupta.event;

import java.time.Instant;

public class BasicEvent {
    private final String userId;

    private final String eventName;

    private final long timestamp;

    public BasicEvent(String userId, String eventName, long timestamp) {
        this.userId = userId;
        this.eventName = eventName;
        this.timestamp = timestamp;
    }

    public BasicEvent(String userId, String eventName) {
        this.userId = userId;
        this.eventName = eventName;
        this.timestamp = Instant.now().getEpochSecond();
    }

    public String getUserId() {
        return userId;
    }

    public String getEventName() {
        return eventName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "BasicEvent{" +
                "userId='" + userId + '\'' +
                ", eventName='" + eventName + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
