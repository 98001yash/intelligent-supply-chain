package com.compay.Inelligent_supply_chain.shipment_service.exceptions;



public class BadRequestException extends RuntimeException{

    public BadRequestException(String message){
        super(message);
    }
}
