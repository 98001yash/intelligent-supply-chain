package com.company.Intelligent_supply_chain.return_service.service;


import com.company.Intelligent_supply_chain.return_service.dtos.RefundResponseDto;
import com.company.Intelligent_supply_chain.return_service.entities.Refund;
import com.company.Intelligent_supply_chain.return_service.repository.RefundRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefundService {

    private final RefundRepository refundRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public RefundResponseDto processRefund(Long returnRequestId, Long orderId,Double amount, String refundMethod){
        log.info("Processing refund for return request ID: {}",returnRequestId);

        // check it refund already esists
        refundRepository.findByReturnRequestId(returnRequestId).ifPresent(existingRefund->{
            throw new RuntimeException("Refund already processed for this return request");
        });


        Refund refund = Refund.builder()
                .returnRequestId(returnRequestId)
                .orderId(orderId)
                .amount(amount)
                .status("INITIATED")
                .refundMethod(refundMethod)
                .processedDate(LocalDateTime.now())
                .build();

        Refund savedRefund = refundRepository.save(refund);
        log.info("Refund processed successfully for Return Request ID: {}", returnRequestId);

        return modelMapper.map(savedRefund, RefundResponseDto.class);
    }

}
