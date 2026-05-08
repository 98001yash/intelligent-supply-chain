package com.company.Intelligent_supply_chain.user_service.dtos;


import com.company.Intelligent_supply_chain.user_service.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDto {

    private String accessToken;

    private String refreshToken;

    private Long userId;

    private String email;

    private Role role;

}