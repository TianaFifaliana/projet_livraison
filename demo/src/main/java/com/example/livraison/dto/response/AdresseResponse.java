package com.example.livraison.dto.response;

public record AdresseResponse(
        Long id,
        String rue,
        String ville,
        String codePostal,
        String pays,
        Double latitude,
        Double longitude,
        Long clientId) {
}
