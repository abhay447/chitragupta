package com.chitragupta.commons.event;

public class EnrichedEvent extends BasicEvent{

    private final String parentEventName;

    private final String grandParentEventName;

    private final long enrichmentTimestamp;


    public EnrichedEvent(String userId, String eventName, long timestamp, String parentEventName, String grandParentEventName, long enrichmentTimestamp) {
        super(userId, eventName, timestamp);
        this.parentEventName = parentEventName;
        this.grandParentEventName = grandParentEventName;
        this.enrichmentTimestamp = enrichmentTimestamp;
    }

    public String getParentEventName() {
        return parentEventName;
    }

    public String getGrandParentEventName() {
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
