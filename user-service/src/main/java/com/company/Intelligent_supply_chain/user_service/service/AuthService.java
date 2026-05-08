package com.company.Intelligent_supply_chain.user_service.service;

import com.company.Intelligent_supply_chain.user_service.dtos.AuthResponseDto;
import com.company.Intelligent_supply_chain.user_service.dtos.LoginRequestDto;
import com.company.Intelligent_supply_chain.user_service.dtos.SignupRequestDto;
import com.company.Intelligent_supply_chain.user_service.dtos.UserDto;
import com.company.Intelligent_supply_chain.user_service.entities.User;
import com.company.Intelligent_supply_chain.user_service.enums.Role;
import com.company.Intelligent_supply_chain.user_service.exceptions.BadRequestException;
import com.company.Intelligent_supply_chain.user_service.exceptions.ResourceNotFoundException;
import com.company.Intelligent_supply_chain.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public UserDto signUp(SignupRequestDto signupRequestDto) {

        log.info("Attempting to register user with email: {}", signupRequestDto.getEmail());

        boolean exists = userRepository.existsByEmail(signupRequestDto.getEmail());

        if (exists) {
            throw new BadRequestException("User already exists with email: "
                    + signupRequestDto.getEmail());
        }

        User user = modelMapper.map(signupRequestDto, User.class);

        // Role Assignment Logic
        if ("ADMIN123".equals(signupRequestDto.getAdminCode())) {
            user.setRole(Role.ADMIN);

        } else if (signupRequestDto.getRole() == null) {
            user.setRole(Role.CUSTOMER);

        } else {
            user.setRole(signupRequestDto.getRole());
        }

        // Encode Password
        user.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));

        // Set User Metadata
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setActive(true);
        user.setEmailVerified(false);

        User savedUser = userRepository.save(user);

        log.info("User registered successfully with ID: {}", savedUser.getId());

        return modelMapper.map(savedUser, UserDto.class);
    }

    public AuthResponseDto login(LoginRequestDto loginRequestDto) {

        log.info("Login attempt for email: {}", loginRequestDto.getEmail());

        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: "
                                        + loginRequestDto.getEmail()
                        ));

        boolean isPasswordMatch = passwordEncoder.matches(
                loginRequestDto.getPassword(),
                user.getPassword()
        );

        if (!isPasswordMatch) {

            log.error("Invalid password for email: {}", loginRequestDto.getEmail());

            throw new BadRequestException("Invalid email or password");
        }

        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Generate Tokens
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        log.info("Login successful for user ID: {}", user.getId());

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public UserDto getUserByEmail(String email) {

        log.info("Fetching user by email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + email
                        ));

        return modelMapper.map(user, UserDto.class);
    }

    public UserDto updateUser(UserDto userDto) {

        log.info("Updating user with email: {}", userDto.getEmail());

        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: "
                                        + userDto.getEmail()
                        ));

        user.setName(userDto.getName());
        user.setRole(userDto.getRole());
        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);

        log.info("User updated successfully with ID: {}", updatedUser.getId());

        return modelMapper.map(updatedUser, UserDto.class);
    }

    public UserDto getUserById(Long userId) {

        log.info("Fetching user by ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + userId
                        ));

        return modelMapper.map(user, UserDto.class);
    }
}