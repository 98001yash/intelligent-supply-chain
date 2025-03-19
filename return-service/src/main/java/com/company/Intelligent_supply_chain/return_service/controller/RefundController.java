package com.company.Intelligent_supply_chain.return_service.controller;


import com.company.Intelligent_supply_chain.return_service.dtos.RefundRequestDto;
import com.company.Intelligent_supply_chain.return_service.dtos.RefundResponseDto;
import com.company.Intelligent_supply_chain.return_service.service.RefundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/returns/refunds")
@RequiredArgsConstructor
@Slf4j
public class RefundController {
    private final RefundService refundService;

    @PostMapping("/process")
    public ResponseEntity<RefundResponseDto> processRefund(@RequestBody RefundRequestDto refundRequestDto) {
        log.info("Received refund request for Return Request ID: {}", refundRequestDto.getReturnRequestId());

        RefundResponseDto response = refundService.processRefund(
                refundRequestDto.getReturnRequestId(),
                refundRequestDto.getOrderId(),
                refundRequestDto.getAmount(),
                refundRequestDto.getRefundMethod()
        );

        return ResponseEntity.ok(response);
    }
}