package com.compay.Inelligent_supply_chain.Payment_service.service;


import com.compay.Inelligent_supply_chain.Payment_service.dtos.PaymentDto;
import com.compay.Inelligent_supply_chain.Payment_service.entities.Payment;
import com.compay.Inelligent_supply_chain.Payment_service.enums.PaymentStatus;
import com.compay.Inelligent_supply_chain.Payment_service.exceptions.ResourceNotFoundException;
import com.compay.Inelligent_supply_chain.Payment_service.reposirtory.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public PaymentDto processPayment(PaymentDto paymentDto){
        log.info("Processing payment for order ID: {}",paymentDto.getOrderId());

        Payment payment = modelMapper.map(paymentDto, Payment.class);
        payment.setStatus(PaymentStatus.PENDING);

        Payment savedPayment =paymentRepository.save(payment);
        log.info("Payment processed successfully with ID: {}",savedPayment.getId());
        return modelMapper.map(savedPayment, PaymentDto.class);
    }

    public PaymentDto getPaymentByOrderId(Long orderId){
        log.info("Fetching payment details for order ID: {}",orderId);

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(()->new ResourceNotFoundException("Payment not found for Order ID: "+orderId));
        return modelMapper.map(payment, PaymentDto.class);
    }

    public PaymentDto updatePaymentStatus(Long paymentId, PaymentStatus status){
        log.info("Updating payment status for payment ID: {} to {}", paymentId, status);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(()->new ResourceNotFoundException("Payment not found with ID: "+paymentId));

        payment.setStatus(status);
        paymentRepository.save(payment);

        log.info("Payment status updated successfully");
        return modelMapper.map(payment, PaymentDto.class);

    }
}
