package com.example.livraison.mapper;

import com.example.livraison.dto.request.CreateIncidentRequest;
import com.example.livraison.dto.response.IncidentResponse;
import com.example.livraison.entity.Incident;
import org.springframework.stereotype.Component;

@Component
public class IncidentMapper {

    public IncidentResponse toResponse(Incident incident) {
        return new IncidentResponse(
                incident.getId(),
                incident.getType() != null ? incident.getType().name() : null,
                incident.getDescription(),
                incident.getDateIncident(),
                Boolean.TRUE.equals(incident.getResolu()),
                incident.getDateResolution(),
                incident.getCommentaireResolution(),
                incident.getCommande() != null ? incident.getCommande().getId() : null,
                incident.getCommande() != null ? incident.getCommande().getNumero() : null,
                incident.getLivreur() != null ? incident.getLivreur().getId() : null,
                incident.getLivreur() != null
                        ? incident.getLivreur().getPrenom() + " " + incident.getLivreur().getNom()
                        : null);
    }

    public Incident toEntity(CreateIncidentRequest request) {
        return Incident.builder()
                .type(request.getType())
                .description(request.getDescription())
                .build();
    }

    public void apply(CreateIncidentRequest request, Incident incident) {
        incident.setType(request.getType());
        incident.setDescription(request.getDescription());
    }
}
