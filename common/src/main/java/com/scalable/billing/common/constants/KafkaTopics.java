package com.scalable.billing.common.constants;

/**
 * Kafka topic constants
 */
public final class KafkaTopics {
    
    public static final String USAGE_EVENTS = "usage-events";
    public static final String BILLING_EVENTS = "billing-events";
    public static final String ANALYTICS_EVENTS = "analytics-events";
    public static final String USAGE_EVENTS_DLQ = "usage-events-dlq";
    
    private KafkaTopics() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
