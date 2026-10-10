package com.example.livraison.mapper;

import com.example.livraison.dto.response.LivraisonResponse;
import com.example.livraison.dto.response.SuiviResponse;
import com.example.livraison.entity.Livraison;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class LivraisonMapper {

    private final SuiviMapper suiviMapper;

    public LivraisonMapper(SuiviMapper suiviMapper) {
        this.suiviMapper = suiviMapper;
    }

    public LivraisonResponse toResponse(Livraison livraison) {
        List<SuiviResponse> suivis = livraison.getSuivis().stream()
                .map(suiviMapper::toResponse)
                .toList();
        return new LivraisonResponse(
                livraison.getId(),
                livraison.getCommande() != null ? livraison.getCommande().getId() : null,
                livraison.getCommande() != null ? livraison.getCommande().getReference() : null,
                livraison.getLivreur() != null ? livraison.getLivreur().getId() : null,
                livraison.getLivreur() != null
                        ? livraison.getLivreur().getPrenom() + " " + livraison.getLivreur().getNom()
                        : null,
                livraison.getDateLivraisonPrevue(),
                livraison.getDateLivraisonReelle(),
                livraison.getStatut().name(),
                suivis);
    }
}
