package com.chitragupta.enricher.dao;

import com.chitragupta.event.EnrichedEvent;

public interface EventJourneyDao {

    public EnrichedEvent getUserJourneyEntry(String userId);

    public void setUserJourneyEntry(EnrichedEvent enrichedEvent, long expiryTimestamp);
}
