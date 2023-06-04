package com.chitragupta.producer;

import com.chitragupta.commons.Constants;
import com.chitragupta.commons.event.BasicEvent;
import com.chitragupta.commons.kafka.EventKafkaProducer;
import com.chitragupta.commons.kafka.KafkaAdminUtils;
import com.google.gson.Gson;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        final long maxUserCount = Long.parseLong(System.getenv().getOrDefault(
                Constants.ENV_MAX_USER_COUNT, "100"));
        final int maxEventCount = Integer.parseInt(System.getenv().getOrDefault(
                Constants.ENV_MAX_EVENT_COUNT, "26"));
        final int eventProduceSleepInterval = Integer.parseInt(System.getenv().getOrDefault(
                Constants.ENV_EVENT_PRODUCE_SLEEP_MILLIS, "100"));

        final MockDataManager mockDataManager = new MockDataManager();
        // kafka props
        final String kafkaUrl = System.getenv(Constants.ENV_KAFKA_URL);
        final Properties properties = new Properties();
        properties.put("bootstrap.servers", kafkaUrl);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // create topic
        KafkaAdminUtils.createTopicIfNotExists(properties, Constants.RAW_EVENT_TOPIC);

        // Create a KafkaProducer instance
        final KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        final EventKafkaProducer eventKafkaProducer = new EventKafkaProducer(producer, Constants.RAW_EVENT_TOPIC);

        // gson
        final Gson gson = new Gson();

        while(true) {
            final BasicEvent basicEvent = mockDataManager.generateEvent(maxUserCount, maxEventCount);
            final String eventAsJsonStr = gson.toJson(basicEvent);
//            System.out.println(basicEvent);
            eventKafkaProducer.sendEvent(eventAsJsonStr);
            Thread.sleep(eventProduceSleepInterval);
        }
    }
}