package com.example.livraison.service;

import com.example.livraison.dto.request.LoginRequest;
import com.example.livraison.dto.request.RegisterRequest;
import com.example.livraison.dto.response.AuthResponse;
import com.example.livraison.dto.response.UserResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    UserResponse currentUser();
}
