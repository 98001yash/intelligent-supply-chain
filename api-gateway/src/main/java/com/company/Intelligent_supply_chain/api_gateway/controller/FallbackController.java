package com.company.Intelligent_supply_chain.api_gateway.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FallbackController {

    @GetMapping("/fallback/payment")
    public ResponseEntity<Map<String, String>> paymentFallback(){
        Map<String, String> response = new HashMap<>();

        response.put("message","Payment service is temporarily unavailable. Please try again later.");
        response.put("status","SERVICE_UNAVAILABLE");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(response);
    }

    @GetMapping("/fallback/inventory")
    public ResponseEntity<Map<String, String>> inventoryFallback(){
        Map<String, String> response =  new HashMap<>();

        response.put("message","inventory service is temporarily unavailable. Please try again later.");
        response.put("status","SERVICE_UNAVAILABLE");

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }


    @GetMapping("/fallback/shipment")
    public ResponseEntity<Map<String, String>>
    shipmentFallback() {

        Map<String, String> response =
                new HashMap<>();
        response.put(
                "message",
                "Shipment Service is temporarily unavailable. Please try again later."
        );
        response.put(
                "status",
                "SERVICE_UNAVAILABLE"
        );
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(response);
    }
}
