package com.chitragupta.commons.event;

import java.util.Optional;

public class EnrichedEvent extends BasicEvent{

    private final Optional<String> parentEventName;

    private final Optional<String> grandParentEventName;

    private final long enrichmentTimestamp;


    public EnrichedEvent(String userId, String eventName, long timestamp, String parentEventName, String grandParentEventName, long enrichmentTimestamp) {
        super(userId, eventName, timestamp);
        this.parentEventName = Optional.of(parentEventName);
        this.grandParentEventName = Optional.of(grandParentEventName);
        this.enrichmentTimestamp = enrichmentTimestamp;
    }

    public Optional<String> getParentEventName() {
        return parentEventName;
    }

    public Optional<String> getGrandParentEventName() {
        return grandParentEventName;
    }

    public long getEnrichmentTimestamp() {
        return enrichmentTimestamp;
    }

    @Override
    public String toString() {
        return "EnrichedEvent{" +
                "parentEventName='" + parentEventName + '\'' +
                ", grandParentEventName='" + grandParentEventName + '\'' +
                ", enrichmentTimestamp=" + enrichmentTimestamp +
                '}';
    }
}
