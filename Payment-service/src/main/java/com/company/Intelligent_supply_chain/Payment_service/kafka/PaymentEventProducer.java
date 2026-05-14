package com.company.Intelligent_supply_chain.Payment_service.kafka;


import com.company.intelligent_supply_chain.events.PaymentProcessedEvent;
import com.company.intelligent_supply_chain.events.RefundProcessedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishPaymentProcessedEvent(
            PaymentProcessedEvent event
    ){
        kafkaTemplate.send("payment-processed-topic",
                event.getOrderId().toString(),
                event);

        log.info("PaymentProcessedEvent published for order Id: {}",
                event.getOrderId());
    }

    public void publishRefundProcessedEvent(
            RefundProcessedEvent event
    ) {

        kafkaTemplate.send(
                "refund-processed-topic",
                event.getOrderId().toString(),
                event
        );

        log.info(
                "RefundProcessedEvent published for Order ID: {}",
                event.getOrderId()
        );
    }
}
