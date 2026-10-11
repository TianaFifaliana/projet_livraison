package com.example.livraison.mapper;

import com.example.livraison.dto.request.PreuveLivraisonRequest;
import com.example.livraison.entity.PreuveLivraison;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class PreuveLivraisonMapper {

    public PreuveLivraison toEntity(PreuveLivraisonRequest request) {
        return PreuveLivraison.builder()
                .nomSignataire(request.getNomSignataire())
                .commentaire(request.getCommentaire())
                .codeConfirmation(request.getCodeConfirmation())
                .dateLivraison(LocalDateTime.now())
                .signature(request.getSignature())
                .photo(request.getPhoto())
                .build();
    }

    public PreuveLivraisonRequest toRequest(PreuveLivraison preuve) {
        return PreuveLivraisonRequest.builder()
                .commandeId(preuve.getCommande() != null ? preuve.getCommande().getId() : null)
                .nomSignataire(preuve.getNomSignataire())
                .commentaire(preuve.getCommentaire())
                .codeConfirmation(preuve.getCodeConfirmation())
                .signature(preuve.getSignature())
                .photo(preuve.getPhoto())
                .build();
    }

    public void apply(PreuveLivraisonRequest request, PreuveLivraison preuve) {
        preuve.setNomSignataire(request.getNomSignataire());
        preuve.setCommentaire(request.getCommentaire());
        preuve.setCodeConfirmation(request.getCodeConfirmation());
        preuve.setSignature(request.getSignature());
        preuve.setPhoto(request.getPhoto());
    }
}
