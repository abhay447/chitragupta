package com.chitragupta.commons.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaAdminUtils {

    public static void createTopic(Properties properties, String topicName) throws ExecutionException, InterruptedException {
        createTopic(properties,topicName,1,1);
    }

    public static void createTopic(Properties properties, String topicName, int partitions, int replicationFactor) throws ExecutionException, InterruptedException {
        try (AdminClient adminClient = AdminClient.create(properties)) {
            NewTopic newTopic = new NewTopic(topicName, partitions, (short) replicationFactor);
            adminClient.createTopics(Collections.singleton(newTopic)).all().get();
        }
    }

}
