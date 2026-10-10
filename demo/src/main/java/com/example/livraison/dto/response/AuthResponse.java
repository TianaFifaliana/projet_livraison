package com.example.livraison.dto.response;

import java.util.Set;

public record AuthResponse(
        String token,
        String type,
        Long userId,
        String username,
        Set<String> roles) {
}
