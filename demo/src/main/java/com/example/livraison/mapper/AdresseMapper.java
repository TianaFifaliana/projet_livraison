package com.example.livraison.mapper;

import com.example.livraison.dto.request.AdresseRequest;
import com.example.livraison.dto.response.AdresseResponse;
import com.example.livraison.entity.Adresse;
import org.springframework.stereotype.Component;

@Component
public class AdresseMapper {

    public AdresseResponse toResponse(Adresse adresse) {
        return new AdresseResponse(
                adresse.getId(),
                adresse.getRue(),
                adresse.getVille(),
                adresse.getCodePostal(),
                adresse.getPays(),
                adresse.getLatitude(),
                adresse.getLongitude(),
                adresse.getClient() != null ? adresse.getClient().getId() : null);
    }

    public Adresse toEntity(AdresseRequest request) {
        return Adresse.builder()
                .rue(request.getRue())
                .ville(request.getVille())
                .codePostal(request.getCodePostal())
                .pays(request.getPays())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();
    }

    public void apply(AdresseRequest request, Adresse adresse) {
        adresse.setRue(request.getRue());
        adresse.setVille(request.getVille());
        adresse.setCodePostal(request.getCodePostal());
        adresse.setPays(request.getPays());
        adresse.setLatitude(request.getLatitude());
        adresse.setLongitude(request.getLongitude());
    }
}
