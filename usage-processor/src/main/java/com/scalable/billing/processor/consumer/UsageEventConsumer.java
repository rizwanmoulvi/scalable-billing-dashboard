package com.scalable.billing.processor.consumer;

import com.scalable.billing.common.event.UsageEvent;
import com.scalable.billing.processor.service.UsageIngestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.scalable.billing.common.constants.KafkaTopics.USAGE_EVENTS;

@Component
@RequiredArgsConstructor
@Slf4j
public class UsageEventConsumer {
    
    private final UsageIngestionService usageIngestionService;
    
    @KafkaListener(
        topics = USAGE_EVENTS,
        groupId = "${spring.kafka.consumer.group-id}",
        concurrency = "3"
    )
    public void consumeUsageEvents(List<UsageEvent> events) {
        log.info("Received {} usage events", events.size());
        usageIngestionService.processUsageEvents(events);
    }
}
