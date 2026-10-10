package com.example.livraison.dto.response;

import java.time.LocalDateTime;

public record SuiviResponse(
        Long id,
        String statut,
        String commentaire,
        String position,
        LocalDateTime date) {
}
