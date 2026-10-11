package com.example.livraison.dto.response;

import java.math.BigDecimal;

public record TarifResponse(
        Long id,
        String libelle,
        BigDecimal prixBase,
        BigDecimal prixParKm,
        BigDecimal prixParKg,
        BigDecimal prixParM3,
        BigDecimal coefficientZone,
        boolean parDefaut,
        Double poidsMax,
        Long zoneId) {
}
