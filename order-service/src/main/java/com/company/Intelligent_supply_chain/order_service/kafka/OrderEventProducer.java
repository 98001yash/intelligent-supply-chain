package com.company.Intelligent_supply_chain.order_service.kafka;


import com.company.intelligent_supply_chain.events.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "order-created-topic";

    public void publishOrderCreatedEvent(
            OrderCreatedEvent event
    ) {
        log.info("Publishing OrderCreatedEvent for Order ID: {}",
                event.getOrderId());
        kafkaTemplate.send(TOPIC, event);
        log.info("OrderCreatedEvent published successfully");
    }
}
