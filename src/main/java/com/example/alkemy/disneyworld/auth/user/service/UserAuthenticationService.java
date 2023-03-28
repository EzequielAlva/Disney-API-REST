package com.example.alkemy.disneyworld.auth.user.service;

import com.example.alkemy.disneyworld.auth.dto.AuthenticationRequest;
import com.example.alkemy.disneyworld.auth.dto.AuthenticationResponse;

public interface UserAuthenticationService {
    AuthenticationResponse register(AuthenticationRequest request);
    AuthenticationResponse login(AuthenticationRequest request);
}
