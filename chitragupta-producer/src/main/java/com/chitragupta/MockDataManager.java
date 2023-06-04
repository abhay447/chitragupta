package com.chitragupta;

import com.chitragupta.event.BasicEvent;

import java.util.Random;
import java.util.UUID;

public class MockDataManager {

    private static final long MAX_USER_COUNT = 1000;
    private static final int MAX_EVENT_COUNT = 26;

    public BasicEvent generateEvent() {
        final Random random = new Random();
        final String eventName = String.valueOf((char)((int)'A' + random.nextInt(MAX_EVENT_COUNT)));
        final String userId = UUID.nameUUIDFromBytes(String.valueOf(random.nextLong(MAX_USER_COUNT)).getBytes()).toString();
        return new BasicEvent(userId, eventName);
    }
}
