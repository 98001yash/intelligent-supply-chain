package com.compay.Inelligent_supply_chain.user_service.dtos;


import com.compay.Inelligent_supply_chain.user_service.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private Role role = Role.CUSTOMER;
}
