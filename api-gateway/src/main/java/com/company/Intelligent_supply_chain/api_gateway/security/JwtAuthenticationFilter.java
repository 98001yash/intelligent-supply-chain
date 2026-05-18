package com.company.Intelligent_supply_chain.api_gateway.security;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain
    ) {

        String path = exchange.getRequest()
                .getURI()
                .getPath();

        // Public Endpoints
        if (path.contains("/auth/login") ||
                path.contains("/auth/signup")) {

            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        // Missing Token
        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            log.error("Missing Authorization Header");

            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {

            String token = authHeader.substring(7);

            boolean isValid = jwtService.validateToken(token);
            if (!isValid) {

                log.error("Invalid JWT token");
                exchange.getResponse()
                        .setStatusCode(HttpStatus.UNAUTHORIZED);

                return exchange.getResponse().setComplete();
            }

            Claims claims = jwtService.extractClaims(token);

            String userId = claims.getSubject();
            String email = claims.get("email", String.class);

            String role = claims.get("role", String.class);

            // INVENTORY APIs -> ADMIN ONLY
            if (path.startsWith("/inventory")
                    && !"ADMIN".equals(role)) {

                log.error(
                        "Access denied for user role: {}",
                        role
                );
                exchange.getResponse()
                        .setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse()
                        .setComplete();
            }

            // SHIPMENT APIs -> ADMIN ONLY
            if (path.startsWith("/shipments")
                    && !"ADMIN".equals(role)) {

                log.error(
                        "Access denied for user role: {}",
                        role
                );
                exchange.getResponse()
                        .setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse()
                        .setComplete();
            }

            log.info("Authenticated user: {}", email);

            // Propagate identity downstream
            ServerHttpRequest mutatedRequest = exchange
                    .getRequest()
                    .mutate()

                    .header("X-User-Id", userId)
                    .header("X-User-Email", email)
                    .header("X-User-Role", role)

                    .build();

            return chain.filter(
                    exchange.mutate()
                            .request(mutatedRequest)
                            .build()
            );

        } catch (Exception e) {

            log.error("JWT authentication failed", e);

            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }

    public void denyAccess(ServerWebExchange exchange) {
        exchange.getResponse()
                .setStatusCode(HttpStatus.FORBIDDEN);

    }
}