package com.compay.Inelligent_supply_chain.shipment_service.service;

import com.compay.Inelligent_supply_chain.shipment_service.dtos.CourierAssignmentDto;
import com.compay.Inelligent_supply_chain.shipment_service.entities.CourierAssignment;
import com.compay.Inelligent_supply_chain.shipment_service.enums.DeliveryStatus;
import com.compay.Inelligent_supply_chain.shipment_service.exceptions.ResourceNotFoundException;
import com.compay.Inelligent_supply_chain.shipment_service.repositories.CourierAssignmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourierAssignmentService {

    private final CourierAssignmentRepository courierAssignmentRepository;
    private final ModelMapper modelMapper;

    public CourierAssignmentDto assignCourier(Long orderId, String courierName) {
        log.info("Assigning courier '{}' to order ID: {}", courierName, orderId);

        CourierAssignment assignment = CourierAssignment.builder()
                .orderId(orderId)
                .courierName(courierName)
                .status(DeliveryStatus.ASSIGNED)
                .assignedAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        CourierAssignment savedAssignment = courierAssignmentRepository.save(assignment);
        log.info("Courier assigned successfully: {}", savedAssignment);

        return modelMapper.map(savedAssignment, CourierAssignmentDto.class);
    }

    public CourierAssignmentDto updateCourierStatus(Long orderId, DeliveryStatus status) {
        log.info("Updating courier status for order ID: {} to {}", orderId, status);

        CourierAssignment assignment = courierAssignmentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Courier assignment not found for order ID: " + orderId));

        assignment.setStatus(status);
        assignment.setUpdatedAt(LocalDateTime.now());

        courierAssignmentRepository.save(assignment);
        log.info("Courier status updated: {}", assignment);
        return modelMapper.map(assignment, CourierAssignmentDto.class);
    }
}
