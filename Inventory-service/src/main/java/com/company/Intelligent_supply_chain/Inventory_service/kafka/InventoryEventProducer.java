package com.company.Intelligent_supply_chain.Inventory_service.kafka;


import com.company.intelligent_supply_chain.events.InventoryReservedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "inventory-reserved-topic";

    public void publishInventoryReservedEvent(
            InventoryReservedEvent event
    ){
        log.info("Publishing InventoryReservedEvent for order Id: {}",
                event.getOrderId());
        kafkaTemplate.send(TOPIC, event);

        log.info("InventoryReservedEvent published successfully");
    }
}
