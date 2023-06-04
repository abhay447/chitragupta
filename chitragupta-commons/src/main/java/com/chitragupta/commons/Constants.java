package com.chitragupta.commons;

public class Constants {

    public static final String RAW_EVENT_TOPIC = "RAW_EVENTS";

    public static final String ENRICHED_EVENT_TOPIC = "ENRICHED_EVENTS";

    public static final String ORIGIN_EVENT_NAME = "ORIGIN";

    public static final long sessionWindowSeconds = 30;

    public static final String ENV_EVENT_PERSISTENCE_PATH = "EVENT_PERSISTENCE_PATH";
    public static final String ENV_KAFKA_URL = "KAFKA_URL";
    public static final String ENV_REDIS_URL = "REDIS_URL";

    public static final String ENV_DRUID_COORDINATOR_URL = "DRUID_COORDINATOR_URL";

    public static final String ENV_MAX_USER_COUNT = "MAX_USER_COUNT";

    public static final String ENV_MAX_EVENT_COUNT = "MAX_EVENT_COUNT";
    public static final String ENV_EVENT_PRODUCE_SLEEP_MILLIS = "EVENT_PRODUCE_SLEEP_MILLIS";
}
