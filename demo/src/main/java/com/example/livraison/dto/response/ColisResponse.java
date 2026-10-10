package com.example.livraison.dto.response;

public record ColisResponse(
        Long id,
        String description,
        Double poids,
        Double longueur,
        Double largeur,
        Double hauteur,
        boolean fragile,
        Long commandeId) {
}
