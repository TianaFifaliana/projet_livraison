package com.example.livraison.dto.response;

public record ColisResponse(
        Long id,
        String description,
        Double poids,
        Double volume,
        boolean fragile,
        Long commandeId) {
}