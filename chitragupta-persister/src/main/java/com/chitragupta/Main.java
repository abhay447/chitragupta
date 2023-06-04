package com.chitragupta;

import com.chitragupta.commons.Constants;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        // output file path
        String outputFilePath = System.getenv().getOrDefault(Constants.ENV_EVENT_PERSISTENCE_PATH,"/tmp/chitragupta_output.log");
        System.out.println(outputFilePath);
        // Kafka Streams configuration
        final String kafkaUrl = System.getenv(Constants.ENV_KAFKA_URL);
        final Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-streams-persister-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // Create a StreamsBuilder
        StreamsBuilder builder = new StreamsBuilder();

        // Create a KStream from the input topic
        KStream<String, String> inputStream = builder.stream(Constants.ENRICHED_EVENT_TOPIC);

        // Redirect the output to a log file
        PrintStream logFile = new PrintStream(outputFilePath);
        inputStream.foreach((key, value) -> logFile.println(value));

        // Build the Kafka Streams application
        KafkaStreams streams = new KafkaStreams(builder.build(), props);

        // Start the Kafka Streams application
        streams.start();

        // Add shutdown hook to gracefully close the Kafka Streams application
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}