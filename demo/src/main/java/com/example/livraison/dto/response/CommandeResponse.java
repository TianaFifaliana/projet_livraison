package com.example.livraison.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CommandeResponse(
        Long id,
        String reference,
        LocalDateTime dateCommande,
        String statut,
        BigDecimal montantTotal,
        Long clientId,
        String clientNom,
        AdresseResponse adresseLivraison,
        List<ColisResponse> colis,
        Long livraisonId,
        Long paiementId) {
}
