package com.example.livraison.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record LivraisonResponse(
        Long id,
        Long commandeId,
        String commandeReference,
        Long livreurId,
        String livreurNom,
        LocalDateTime dateLivraisonPrevue,
        LocalDateTime dateLivraisonReelle,
        String statut,
        List<SuiviResponse> suivis) {
}
