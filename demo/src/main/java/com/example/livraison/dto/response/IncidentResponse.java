package com.example.livraison.dto.response;

import java.time.LocalDateTime;

public record IncidentResponse(
        Long id,
        String type,
        String description,
        LocalDateTime dateIncident,
        boolean resolu,
        LocalDateTime dateResolution,
        String commentaireResolution,
        Long commandeId,
        String commandeNumero,
        Long livreurId,
        String livreurNom) {
}
