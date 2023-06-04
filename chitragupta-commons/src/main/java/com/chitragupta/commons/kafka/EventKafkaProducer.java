package com.chitragupta.commons.kafka;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class EventKafkaProducer {

    private final Producer<String, String> producer;
    private final String outTopic;

    public EventKafkaProducer(Producer<String, String> producer, String outTopic) {
        this.producer = producer;
        this.outTopic = outTopic;
    }

    public void sendEvent(String eventAsString) {
        final ProducerRecord<String , String> producerRecord = new ProducerRecord<>(
            outTopic, eventAsString
        );
        this.producer.send(producerRecord);
    }
}
