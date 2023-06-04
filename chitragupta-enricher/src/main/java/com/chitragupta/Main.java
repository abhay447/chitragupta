package com.chitragupta;

import com.chitragupta.commons.Constants;
import com.chitragupta.commons.kafka.CustomKafkaStreamsExceptionHandler;
import com.chitragupta.commons.kafka.KafkaAdminUtils;
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
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // jedis
        final String redisUrl = System.getenv(Constants.ENV_REDIS_URL);
        final Jedis jedis = new Jedis(redisUrl);
        // kafka props
        final String kafkaUrl = System.getenv(Constants.ENV_KAFKA_URL);
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-streams-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // create kafka topic
        KafkaAdminUtils.createTopicIfNotExists(props, Constants.ENRICHED_EVENT_TOPIC);

        final EnrichmentKafkaStream enrichmentKafkaStream =
                new EnrichmentKafkaStream(new Gson(), new RedisEventJourneyDao(jedis, new Gson()), Constants.sessionWindowSeconds);
        KafkaStreams streams = enrichmentKafkaStream.buildEnrichmentStream(props, Constants.RAW_EVENT_TOPIC, Constants.sessionWindowSeconds, Constants.ENRICHED_EVENT_TOPIC);
        streams.setUncaughtExceptionHandler(new CustomKafkaStreamsExceptionHandler());
        // Start the Kafka Streams application
        streams.start();

        try {
            // Add an infinite loop to keep the application running
            while (true) {
                 System.out.println(streams.state());
                Thread.sleep(1000); // Adjust the sleep duration as needed
            }
        } catch (InterruptedException e) {
            // Handle any InterruptedException gracefully
            e.printStackTrace();
        } finally {
            // Shutdown Kafka Streams before exiting
            streams.close();
        }

        // Add shutdown hook to gracefully close the Kafka Streams application
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

    }
}