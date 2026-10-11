package com.example.livraison.dto.response;

public record LivreurResponse(
        Long id,
        String nom,
        String prenom,
        String telephone,
        String email,
        String statut,
        Long zoneId,
        Long vehiculeId,
        String vehiculeImmatriculation,
        Long utilisateurId) {
}