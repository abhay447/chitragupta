package com.chitragupta.event;

public class BasicEvent {
    private final String userId;

    private final String eventName;

    private final long timestamp;

    public BasicEvent(String userId, String eventName, long timestamp) {
        this.userId = userId;
        this.eventName = eventName;
        this.timestamp = timestamp;
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
}
