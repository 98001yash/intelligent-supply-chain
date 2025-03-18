package com.company.Intelligent_supply_chain.return_service.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReturnStatus {

    private Long returnId;
    private String status;
}
