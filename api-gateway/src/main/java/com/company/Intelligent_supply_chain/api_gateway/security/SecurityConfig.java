package com.company.Intelligent_supply_chain.api_gateway.security;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.web.server.ServerHttpSecurity;

import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http
    ) {

        return http

                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                .formLogin(
                        ServerHttpSecurity
                                .FormLoginSpec::disable
                )

                .httpBasic(
                        ServerHttpSecurity
                                .HttpBasicSpec::disable
                )

                .authorizeExchange(exchange -> exchange

                        .pathMatchers(
                                "/auth/login",
                                "/auth/signup"
                        )

                        .permitAll()

                        // IMPORTANT
                        .anyExchange().permitAll()
                )

                .build();
    }
}