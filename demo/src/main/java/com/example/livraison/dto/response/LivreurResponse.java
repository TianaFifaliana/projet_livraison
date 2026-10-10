package com.example.livraison.dto.response;

public record LivreurResponse(
        Long id,
        String nom,
        String prenom,
        String telephone,
        String email,
        String vehicule,
        boolean disponible,
        Long userId) {
}
