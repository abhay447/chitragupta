package com.chitragupta;

import com.chitragupta.commons.BasicRetryUtils;
import com.chitragupta.commons.Constants;
import com.chitragupta.commons.druid.DruidClient;
import com.chitragupta.commons.kafka.CustomKafkaStreamsExceptionHandler;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;

public class Main {

    private static final String ENRICHED_EVENT_DRUID_SPEC_FILE_PATH = "druid/enriched_events_druid_spec.json";

    private static void submitDruidIngestionSpec(DruidClient druidClient, String enrichedEventDruidSpecPath) throws URISyntaxException, IOException, InterruptedException {
        final String enrichedEventDruidSpec = Files.readString(
                Path.of(Thread.currentThread().getContextClassLoader().getResource(enrichedEventDruidSpecPath).toURI()));
        BasicRetryUtils
                .runWithRetries(5, druidClient::submitIngestionSpec, enrichedEventDruidSpec);
    }

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        // output file path
        String outputFilePath = System.getenv().getOrDefault(Constants.ENV_EVENT_PERSISTENCE_PATH,"/tmp/chitragupta_output.log");

        // Kafka Streams configuration
        final String kafkaUrl = System.getenv(Constants.ENV_KAFKA_URL);
        final Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-streams-persister-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // setup druid ingestion spec
        final String druidCoordinatorUrl = System.getenv(Constants.ENV_DRUID_COORDINATOR_URL);
        final DruidClient druidClient = new DruidClient(druidCoordinatorUrl);
        submitDruidIngestionSpec(druidClient,ENRICHED_EVENT_DRUID_SPEC_FILE_PATH);

        // Create kafka stream
        final StreamsBuilder builder = new StreamsBuilder();
        final KStream<String, String> inputStream = builder.stream(Constants.ENRICHED_EVENT_TOPIC);
        final PrintStream logFile = new PrintStream(outputFilePath);
        inputStream.foreach((key, value) -> logFile.println(value));
        final KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.setUncaughtExceptionHandler(new CustomKafkaStreamsExceptionHandler());
        streams.start();

        try {
            // Add an infinite loop to keep the application running
            while (true) {
//                System.out.println(streams.state());
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