package com.company.Intelligent_supply_chain.order_service.exceptions;



public class BadRequestException extends RuntimeException{

    public BadRequestException(String message){
        super(message);
    }
}
