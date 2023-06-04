package com.chitragupta;

import com.chitragupta.commons.Constants;
import com.chitragupta.enricher.EnrichmentKafkaStream;
import com.chitragupta.enricher.dao.RedisEventJourneyDao;
import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import redis.clients.jedis.Jedis;

import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        // jedis
        final Jedis jedis = new Jedis("localhost");
        // kafka props
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-streams-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        final EnrichmentKafkaStream enrichmentKafkaStream =
                new EnrichmentKafkaStream(new Gson(), new RedisEventJourneyDao(jedis, new Gson()), Constants.sessionWindowSeconds);
        final StreamsBuilder builder = new StreamsBuilder();
        final KStream<String , String> enrichmentStream = enrichmentKafkaStream.buildEnrichmentStream(Constants.RAW_EVENT_TOPIC, builder, Constants.sessionWindowSeconds);
        enrichmentStream.to(Constants.ENRICHED_EVENT_TOPIC, Produced.with(Serdes.String(), Serdes.String()));
        KafkaStreams streams = new KafkaStreams(builder.build(), props);

        // Start the Kafka Streams application
        streams.start();

        // Add shutdown hook to gracefully close the Kafka Streams application
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

    }
}