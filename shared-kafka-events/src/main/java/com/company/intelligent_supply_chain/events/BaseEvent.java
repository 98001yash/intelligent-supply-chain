package com.company.intelligent_supply_chain.events;


import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseEvent {

    private String eventId;
    private String eventType;
    private String correlationId;
    private LocalDateTime timestamp;

    public static String generateEventId() {
        return UUID.randomUUID().toString();
    }
}