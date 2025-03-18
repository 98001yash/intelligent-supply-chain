package com.company.Intelligent_supply_chain.return_service.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnRequest {

    private Long orderId;
    private String reason;
    private LocalDateTime requestDate;
    private String status;  //  PENDING, APPROVED. REJECTED
}
