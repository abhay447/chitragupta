package com.chitragupta.enricher.dao;

import com.chitragupta.commons.event.EnrichedEvent;

import java.util.Optional;

public interface EventJourneyDao {

    public Optional<EnrichedEvent> getUserJourneyEntry(String userId);

    public void setUserJourneyEntry(EnrichedEvent enrichedEvent, long expiryTimestamp);
}
