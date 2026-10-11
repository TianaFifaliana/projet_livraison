package com.example.livraison.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CommandeResponse(
        Long id,
        String numero,
        String adresseEnlevement,
        String adresseLivraison,
        Double poids,
        Double volume,
        String priorite,
        Double distance,
        BigDecimal prix,
        LocalDateTime datePrevue,
        LocalDateTime dateCreation,
        String statut,
        Long clientId,
        String clientNom,
        Long livreurId,
        Long zoneId) {
}