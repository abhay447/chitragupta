package com.chitragupta.enricher.dao;

import com.chitragupta.commons.event.EnrichedEvent;

import java.util.Optional;

public class RedisEventJourneyDao implements EventJourneyDao{
    @Override
    public Optional<EnrichedEvent> getUserJourneyEntry(String userId) {
        return Optional.empty();
    }

    @Override
    public void setUserJourneyEntry(EnrichedEvent enrichedEvent, long expiryTimestamp) {

    }
}
