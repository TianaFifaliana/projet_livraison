package com.example.livraison.mapper;

import com.example.livraison.dto.request.PaiementRequest;
import com.example.livraison.dto.response.PaiementResponse;
import com.example.livraison.entity.Paiement;
import org.springframework.stereotype.Component;

@Component
public class PaiementMapper {

    public PaiementResponse toResponse(Paiement paiement) {
        return new PaiementResponse(
                paiement.getId(),
                paiement.getCommande() != null ? paiement.getCommande().getId() : null,
                paiement.getCommande() != null ? paiement.getCommande().getNumero() : null,
                paiement.getMontant(),
                paiement.getMethode().name(),
                paiement.getStatut().name(),
                paiement.getDatePaiement(),
                paiement.getTransactionId());
    }

    public Paiement toEntity(PaiementRequest request) {
        return Paiement.builder()
                .montant(request.getMontant())
                .methode(request.getMethode())
                .statut(request.getStatut())
                .transactionId(request.getTransactionId())
                .build();
    }
}
