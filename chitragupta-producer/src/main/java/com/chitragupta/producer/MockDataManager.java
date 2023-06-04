package com.chitragupta.producer;

import com.chitragupta.commons.event.BasicEvent;

import java.util.Random;
import java.util.UUID;

public class MockDataManager {


    public BasicEvent generateEvent(long maxUserCount, int maxEventCount) {
        final Random random = new Random();
        final String eventName = String.valueOf((char)((int)'A' + random.nextInt(maxEventCount)));
        final String userId = UUID.nameUUIDFromBytes(String.valueOf(random.nextLong(maxUserCount)).getBytes()).toString();
        return new BasicEvent(userId, eventName);
    }
}
