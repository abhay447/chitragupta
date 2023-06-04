package com.chitragupta.enricher;

import com.chitragupta.commons.Constants;
import com.chitragupta.commons.event.BasicEvent;
import com.chitragupta.commons.event.EnrichedEvent;
import com.chitragupta.enricher.dao.EventJourneyDao;
import com.google.gson.Gson;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;

import java.time.Instant;
import java.util.Optional;

public class EnrichmentKafkaStream {

    private final Gson gson;
    private final EventJourneyDao eventJourneyDao;

    private final long sessionWindowSeconds;

    public EnrichmentKafkaStream(Gson gson, EventJourneyDao eventJourneyDao, long sessionWindowSeconds) {
        this.gson = gson;
        this.eventJourneyDao = eventJourneyDao;
        this.sessionWindowSeconds = sessionWindowSeconds;
    }

    private BasicEvent readJsonEvent(String eventBody) {
        return gson.fromJson(eventBody, BasicEvent.class);
    }

    private EnrichedEvent enrichedEvent(BasicEvent basicEvent, long expiryTime) {
        final Optional<EnrichedEvent> userJourney = this.eventJourneyDao.getUserJourneyEntry(basicEvent.getUserId());
        final EnrichedEvent enrichedEvent = new EnrichedEvent(
                basicEvent.getUserId(),
                basicEvent.getEventName(),
                basicEvent.getTimestamp(),
                userJourney.map(EnrichedEvent::getEventName).orElse(Constants.ORIGIN_EVENT_NAME),
                userJourney.flatMap(EnrichedEvent::getParentEventName).orElse(Constants.ORIGIN_EVENT_NAME),
                Instant.now().getEpochSecond()
        );
        this.eventJourneyDao.setUserJourneyEntry(enrichedEvent, expiryTime );
        return enrichedEvent;
    }

    public KStream<String, String> buildEnrichmentStream(String inputKafkaTopic, StreamsBuilder builder, long expiryTime) {
        final KStream<String, String> inputStream = builder.stream(inputKafkaTopic);
        final KTable<String, String> enrichmentFlow = inputStream
                .mapValues(value -> readJsonEvent(value))
                .mapValues(basicEvent -> enrichedEvent(basicEvent, expiryTime))
                .mapValues(enrichedEvent -> gson.toJson(enrichedEvent))
                .toTable();
        return enrichmentFlow.toStream();
    }
}
