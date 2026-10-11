package com.example.livraison.mapper;

import com.example.livraison.dto.request.ColisRequest;
import com.example.livraison.dto.response.ColisResponse;
import com.example.livraison.entity.Colis;
import org.springframework.stereotype.Component;

@Component
public class ColisMapper {

    public ColisResponse toResponse(Colis colis) {
        return new ColisResponse(
                colis.getId(),
                colis.getDescription(),
                colis.getPoids(),
                colis.getVolume(),
                colis.isFragile(),
                colis.getCommande() != null ? colis.getCommande().getId() : null);
    }

    public Colis toEntity(ColisRequest request) {
        return Colis.builder()
                .description(request.getDescription())
                .poids(request.getPoids())
                .volume(request.getVolume())
                .fragile(request.isFragile())
                .build();
    }

    public void apply(ColisRequest request, Colis colis) {
        colis.setDescription(request.getDescription());
        colis.setPoids(request.getPoids());
        colis.setVolume(request.getVolume());
        colis.setFragile(request.isFragile());
    }
}