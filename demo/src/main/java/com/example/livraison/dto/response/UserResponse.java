package com.example.livraison.dto.response;

import java.util.Set;

public record UserResponse(
        Long id,
        String username,
        String email,
        boolean enabled,
        Set<String> roles) {
}
