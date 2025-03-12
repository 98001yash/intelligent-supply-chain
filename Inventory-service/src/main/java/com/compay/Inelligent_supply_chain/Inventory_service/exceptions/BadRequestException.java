package com.compay.Inelligent_supply_chain.Inventory_service.exceptions;



public class BadRequestException extends RuntimeException{

    public BadRequestException(String message){
        super(message);
    }
}
