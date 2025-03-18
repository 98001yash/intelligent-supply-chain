package com.company.Intelligent_supply_chain.return_service.exceptions;


import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiError {

    private LocalDateTime timeStamp;
    private String error;
    private HttpStatus statusCode;

    public ApiError(){
        this.timeStamp = LocalDateTime.now();
    }
    public ApiError(String error, HttpStatus statusCode){
        this.error  = error;
        this.statusCode = statusCode;
    }
}
