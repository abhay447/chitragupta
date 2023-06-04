package com.chitragupta.enricher.dao;

import com.chitragupta.commons.event.EnrichedEvent;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

import java.util.Optional;

public class RedisEventJourneyDao implements EventJourneyDao{

    private final Jedis jedis;

    private final Gson gson;

    public RedisEventJourneyDao(Jedis jedis, Gson gson) {
        this.jedis = jedis;
        this.gson = gson;
    }

    @Override
    public Optional<EnrichedEvent> getUserJourneyEntry(String userId) {
        return Optional.ofNullable(jedis.get(userId)).map(val -> gson.fromJson(val,EnrichedEvent.class));
    }

    @Override
    public void setUserJourneyEntry(EnrichedEvent enrichedEvent, long expiryTime) {
        jedis.setex(enrichedEvent.getUserId(), expiryTime, gson.toJson(enrichedEvent));
    }
}
