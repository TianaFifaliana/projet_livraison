package com.example.livraison.dto.response;

import java.math.BigDecimal;

public record PrixResponse(
        BigDecimal montant,
        BigDecimal prixBase,
        BigDecimal prixParKm,
        BigDecimal prixParKg,
        BigDecimal prixParM3,
        BigDecimal coefficientPriorite,
        BigDecimal coefficientZone,
        Long tarifId,
        String tarifLibelle,
        boolean tarifParDefaut) {
}
