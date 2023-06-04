package com.chitragupta.commons.kafka;

import org.apache.kafka.streams.errors.StreamsException;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;

public class CustomKafkaStreamsExceptionHandler implements StreamsUncaughtExceptionHandler {

    @Override
    public StreamThreadExceptionResponse handle(Throwable exception) {
        exception.printStackTrace();
        if (exception instanceof StreamsException) {
            // Print the exception message or perform custom error handling
            System.err.println("Kafka Streams Exception: " + exception.getMessage());
        } else {
            // Print other types of exceptions
            System.err.println("Exception: " + exception.getMessage());
        }
        return StreamThreadExceptionResponse.SHUTDOWN_APPLICATION;
    }
}
