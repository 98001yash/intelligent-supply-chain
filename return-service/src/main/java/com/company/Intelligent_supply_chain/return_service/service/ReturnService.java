package com.company.Intelligent_supply_chain.return_service.service;


import com.company.Intelligent_supply_chain.return_service.dtos.ReturnRequestDto;
import com.company.Intelligent_supply_chain.return_service.dtos.ReturnResponseDto;
import com.company.Intelligent_supply_chain.return_service.entities.ReturnRequest;
import com.company.Intelligent_supply_chain.return_service.exceptions.ResourceNotFoundException;
import com.company.Intelligent_supply_chain.return_service.repository.ReturnRequestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReturnService {

    private final ReturnRequestRepository returnRequestRepository;
    private final ModelMapper modelMapper;


    @Transactional
    public ReturnResponseDto createReturn(ReturnRequestDto returnRequestDto) {
        log.info(" Creating return request for Order ID: {}", returnRequestDto.getOrderId());

        ReturnRequest returnRequest = modelMapper.map(returnRequestDto, ReturnRequest.class);
        returnRequest.setRequestDate(LocalDateTime.now());
        returnRequest.setStatus("PENDING"); // Default status

        ReturnRequest savedRequest = returnRequestRepository.save(returnRequest);
        log.info(" Return request created successfully for Order ID: {}", savedRequest.getOrderId());
        return modelMapper.map(savedRequest, ReturnResponseDto.class);
    }


    @Transactional
    public ReturnResponseDto getReturnByOrderId(Long orderId) {
        log.info(" Fetching return request for Order ID: {}", orderId);

        ReturnRequestDto returnRequest = returnRequestRepository.findByOrderId(orderId)
                .orElseThrow(() -> {
                    log.warn(" Return request not found for Order ID: {}", orderId);
                    return new ResourceNotFoundException("Return request not found for order: " + orderId);
                });

        log.info(" Return request found for Order ID: {}", orderId);
        return modelMapper.map(returnRequest, ReturnResponseDto.class);
    }


    // get all tbe return Requests
    public List<ReturnResponseDto> getAllReturns() {
        log.info("Fetching all the return request");
        List<ReturnRequest> returnRequests = returnRequestRepository.findAll();

        return returnRequests.stream()
                .map(request->modelMapper.map(request, ReturnResponseDto.class))
                .collect(Collectors.toList());

    }


    // update the return Request status
    public ReturnResponseDto updateReturnStatus(Long id, String status) {
        log.info("Updating return request ID: {} to  status: {}", id, status);

        ReturnRequest returnRequest = returnRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Return request not found with ID: " + id));
        returnRequest.setStatus(status);
        ReturnRequest updatedRequest = returnRequestRepository.save(returnRequest);
        log.info("Return request ID: {} updated successfully to status: {}", id, status);
        return modelMapper.map(updatedRequest, ReturnResponseDto.class);
    }
}

