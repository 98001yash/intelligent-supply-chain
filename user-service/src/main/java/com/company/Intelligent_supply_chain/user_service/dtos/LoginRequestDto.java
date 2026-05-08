package com.company.Intelligent_supply_chain.user_service.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}