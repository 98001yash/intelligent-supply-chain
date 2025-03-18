package com.company.Intelligent_supply_chain.return_service.controller;


import com.company.Intelligent_supply_chain.return_service.dtos.ReturnRequestDto;
import com.company.Intelligent_supply_chain.return_service.dtos.ReturnResponseDto;
import com.company.Intelligent_supply_chain.return_service.service.ReturnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/returns")
@RequiredArgsConstructor
@Slf4j
public class ReturnController {


    private final ReturnService returnService;


    @PostMapping
    public ResponseEntity<ReturnResponseDto> createReturn(@RequestBody ReturnRequestDto returnRequestDto){
      log.info("Received request to create return for Order ID: "+returnRequestDto.getOrderId());
      ReturnResponseDto returnList = returnService.createReturn(returnRequestDto);
      return ResponseEntity.ok(returnList);
    }


    @GetMapping
    public ResponseEntity<List<ReturnResponseDto>> getAllReturns() {
        log.info("Fetching all return requests");
        List<ReturnResponseDto> returnList = returnService.getAllReturns();
        return ResponseEntity.ok(returnList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnResponseDto>  getReturnById(@PathVariable Long id){
        log.info("Fetching return request for ID: "+id);
        ReturnResponseDto returnResponse = returnService.getReturnByOrderId(id);
        return ResponseEntity.ok(returnResponse);
    }


    public ResponseEntity<ReturnResponseDto> updateReturnStatus(@PathVariable Long id,
                                                                @RequestParam String status){
        log.info("updating return  request ID {} to status {}", id, status);
        ReturnResponseDto updatedReturn = returnService.updateReturnStatus(id, status);
        return ResponseEntity.ok(updatedReturn);
    }

}
