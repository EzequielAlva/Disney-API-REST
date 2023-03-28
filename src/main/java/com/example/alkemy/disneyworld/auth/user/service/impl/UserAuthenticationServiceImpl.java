package com.example.alkemy.disneyworld.auth.user.service.impl;

import com.example.alkemy.disneyworld.auth.dto.AuthenticationRequest;
import com.example.alkemy.disneyworld.auth.dto.AuthenticationResponse;
import com.example.alkemy.disneyworld.auth.jwt.JwtService;
import com.example.alkemy.disneyworld.auth.user.Role;
import com.example.alkemy.disneyworld.auth.user.UserEntity;
import com.example.alkemy.disneyworld.auth.user.UserRepository;
import com.example.alkemy.disneyworld.auth.user.service.UserAuthenticationService;
import com.example.alkemy.disneyworld.exception.UserAlreadyExistException;
import com.example.alkemy.disneyworld.exception.UserNotFoundException;
import com.example.alkemy.disneyworld.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private static final String ALREADY_EXIST_MESSAGE = "This user already exist.";

    private static final String NOT_FOUND_MESSAGE = "This user do not exist.";

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final EmailService emailService;

    @Override
    public AuthenticationResponse register(AuthenticationRequest request) {
        Optional<UserEntity> userConfirmation = userRepository.getUserByUsername(request.getUsername());
        if(!userConfirmation.isEmpty()){
            throw new UserAlreadyExistException(ALREADY_EXIST_MESSAGE);
        }

        UserEntity user = null;
        if(request.getUsername().contains("disneyadmin")) {
            user = UserEntity.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ADMIN)
                    .build();
        } else {
            user = UserEntity.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .build();
        }
        userRepository.save(user);
        emailService.sendWelcomeEmailTo(user.getUsername());

        String jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .jwt(jwt)
                .build();
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        Optional<UserEntity> userConfirmation = userRepository.getUserByUsername(request.getUsername());
        if(userConfirmation.isEmpty()){
            throw new UserNotFoundException(NOT_FOUND_MESSAGE);
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserEntity user = userRepository.getUserByUsername(request.getUsername()).get();

        String jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .jwt(jwt)
                .build();
    }
}
