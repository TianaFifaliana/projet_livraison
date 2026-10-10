package com.example.livraison.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaiementResponse(
        Long id,
        Long commandeId,
        String commandeReference,
        BigDecimal montant,
        String methode,
        String statut,
        LocalDateTime datePaiement,
        String transactionId) {
}
