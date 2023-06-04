package com.chitragupta.commons.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaAdminUtils {

    public static void createTopicIfNotExists(Properties properties, String topicName) throws ExecutionException, InterruptedException {
        createTopicIfNotExists(properties,topicName,1,1);
    }

    public static void createTopicIfNotExists(Properties properties, String topicName, int partitions, int replicationFactor) throws ExecutionException, InterruptedException {
        if(checkIfTopicExists(properties, topicName)) {
            return;
        }
        try (AdminClient adminClient = AdminClient.create(properties)) {
            NewTopic newTopic = new NewTopic(topicName, partitions, (short) replicationFactor);
            adminClient.createTopics(Collections.singleton(newTopic)).all().get();
        }
    }

    public static boolean checkIfTopicExists(Properties properties, String topicName) throws ExecutionException, InterruptedException {
        try (AdminClient adminClient = AdminClient.create(properties)) {
            return adminClient.listTopics().names().get().contains(topicName);
        }
    }

}
