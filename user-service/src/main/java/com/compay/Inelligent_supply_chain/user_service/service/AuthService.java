package com.compay.Inelligent_supply_chain.user_service.service;


import com.compay.Inelligent_supply_chain.user_service.dtos.AuthRequestDto;
import com.compay.Inelligent_supply_chain.user_service.dtos.UserDto;
import com.compay.Inelligent_supply_chain.user_service.entities.User;
import com.compay.Inelligent_supply_chain.user_service.enums.Role;
import com.compay.Inelligent_supply_chain.user_service.exceptions.BadRequestException;
import com.compay.Inelligent_supply_chain.user_service.exceptions.ResourceNotFoundException;
import com.compay.Inelligent_supply_chain.user_service.repository.UserRepository;
import com.compay.Inelligent_supply_chain.user_service.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ResourceClosedException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtTokenProvider jwtTokenProvider;

    public UserDto signUp(AuthRequestDto authRequestDto){
        // chcek if the user already exists
        boolean exists = userRepository.existsByEmail(authRequestDto.getEmail());
        if(exists){
            throw new BadRequestException("User already exists, cannot signup again");
        }

        // map request to User entity
        User user = modelMapper.map(authRequestDto, User.class);

        // check if the admin code is provided and valid
     if("ADMIN123".equals(authRequestDto.getAdminCode())){
         user.setRole(Role.ADMIN);
     }else if(authRequestDto.getRole()==null){
         user.setRole(Role.CUSTOMER);
     }else{
         user.setRole(authRequestDto.getRole());
     }

        //hash the password before saving
        user.setPassword(PasswordUtils.hashPassword(authRequestDto.getPassword()));


     // save the user in the database
        User savedUser = userRepository.save(user);

        // map saved User entity to UserDto for response
        return modelMapper.map(savedUser, UserDto.class);
    }

    public String login(AuthRequestDto authRequestDto){
        User user = userRepository.findByEmail(authRequestDto.getEmail())
                .orElseThrow(()->new ResourceNotFoundException("User not found with email: "+authRequestDto.getEmail()));
        boolean isPasswordMatch = PasswordUtils.checkPassword(authRequestDto.getPassword(), user.getPassword());

        if(!isPasswordMatch){
            throw new BadRequestException("Incorrect password");
        }
        return jwtTokenProvider.generateAccessToken(user);
    }

    public UserDto getUserByEmail(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new ResourceNotFoundException("User not found with email: "+email));
        return modelMapper.map(user, UserDto.class);
    }

    public UserDto updateUser(UserDto userDto){
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(()->new ResourceNotFoundException("User not found with email: "+userDto.getEmail()));

        // update User details
        user.setName(userDto.getName());
        user.setRole(userDto.getRole());
        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    public UserDto getUserById(Long userId){
        log.info("Fetching user By ID: {}",userId);

        User user = userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User not found with ID: "+userId));

        return new UserDto(user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole());
    }
}
