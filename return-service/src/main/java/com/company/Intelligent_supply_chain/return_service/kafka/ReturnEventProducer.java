package com.company.Intelligent_supply_chain.return_service.kafka;

import com.company.intelligent_supply_chain.events.ReturnRequestedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReturnEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC =
            "return-requested-topic";

    public void publishReturnRequestedEvent(
            ReturnRequestedEvent event
    ) {

        log.info(
                "Publishing ReturnRequestedEvent for Order ID: {}",
                event.getOrderId()
        );

        kafkaTemplate.send(
                TOPIC,
                event.getOrderId().toString(),
                event
        );
        log.info(
                "ReturnRequestedEvent published successfully for Order ID: {}",
                event.getOrderId()
        );
    }
}