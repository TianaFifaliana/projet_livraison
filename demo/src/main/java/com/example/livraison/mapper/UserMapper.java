package com.example.livraison.mapper;

import com.example.livraison.dto.request.RegisterRequest;
import com.example.livraison.dto.request.UserRequest;
import com.example.livraison.dto.response.UserResponse;
import com.example.livraison.entity.Utilisateur;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(Utilisateur utilisateur) {
        return new UserResponse(
                utilisateur.getId(),
                utilisateur.getUsername(),
                utilisateur.getEmail(),
                utilisateur.isActif(),
                Set.of(utilisateur.getRole().name()));
    }

    public Utilisateur toEntity(UserRequest request) {
        return Utilisateur.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .actif(request.isEnabled())
                .role(request.getRole())
                .build();
    }

    public Utilisateur toEntity(RegisterRequest request) {
        return Utilisateur.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .actif(true)
                .role(request.getRole())
                .build();
    }

    public void apply(UserRequest request, Utilisateur utilisateur) {
        utilisateur.setUsername(request.getUsername());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setActif(request.isEnabled());
        if (request.getRole() != null) {
            utilisateur.setRole(request.getRole());
        }
    }
}