package com.company.Intelligent_supply_chain.user_service.controller;

import com.company.Intelligent_supply_chain.user_service.dtos.AuthResponseDto;
import com.company.Intelligent_supply_chain.user_service.dtos.LoginRequestDto;
import com.company.Intelligent_supply_chain.user_service.dtos.SignupRequestDto;
import com.company.Intelligent_supply_chain.user_service.dtos.UserDto;
import com.company.Intelligent_supply_chain.user_service.service.AuthService;
import com.company.Intelligent_supply_chain.user_service.service.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(
            @Valid @RequestBody SignupRequestDto signupRequestDto) {

        log.info("Received signup request for email: {}",
                signupRequestDto.getEmail());

        UserDto userDto = authService.signUp(signupRequestDto);

        log.info("User signup successful for email: {}",
                signupRequestDto.getEmail());
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @Valid @RequestBody LoginRequestDto loginRequestDto) {

        log.info("Received login request for email: {}",
                loginRequestDto.getEmail());

        AuthResponseDto authResponse =
                authService.login(loginRequestDto);

        log.info("Login successful for email: {}",
                loginRequestDto.getEmail());
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(
            @RequestParam("token") String token) {

        log.info("Validating JWT token");

        boolean isValid = jwtTokenProvider.validateToken(token);
        return ResponseEntity.ok(isValid);
    }


    @GetMapping("/user/{email}")
    public ResponseEntity<UserDto> getUserByEmail(
            @PathVariable String email) {

        log.info("Fetching user by email: {}", email);

        UserDto userDto = authService.getUserByEmail(email);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/user/id/{userId}")
    public ResponseEntity<UserDto> getUserById(
            @PathVariable Long userId) {

        log.info("Fetching user by ID: {}", userId);

        UserDto userDto = authService.getUserById(userId);
        return ResponseEntity.ok(userDto);
    }
}