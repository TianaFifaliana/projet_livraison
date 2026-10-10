package com.example.livraison.mapper;

import com.example.livraison.dto.request.SuiviRequest;
import com.example.livraison.dto.response.SuiviResponse;
import com.example.livraison.entity.Suivi;
import org.springframework.stereotype.Component;

@Component
public class SuiviMapper {

    public SuiviResponse toResponse(Suivi suivi) {
        return new SuiviResponse(
                suivi.getId(),
                suivi.getStatut().name(),
                suivi.getCommentaire(),
                suivi.getPosition(),
                suivi.getDate());
    }

    public Suivi toEntity(SuiviRequest request) {
        return Suivi.builder()
                .statut(request.getStatut())
                .commentaire(request.getCommentaire())
                .position(request.getPosition())
                .build();
    }
}
