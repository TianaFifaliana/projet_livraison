package com.example.livraison.mapper;

import com.example.livraison.dto.request.TarifRequest;
import com.example.livraison.dto.response.TarifResponse;
import com.example.livraison.entity.Tarif;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class TarifMapper {

    public TarifResponse toResponse(Tarif tarif) {
        return new TarifResponse(
                tarif.getId(),
                tarif.getLibelle(),
                tarif.getPrixBase(),
                tarif.getPrixParKm(),
                tarif.getPrixParKg(),
                tarif.getPrixParM3(),
                tarif.getCoefficientZone(),
                Boolean.TRUE.equals(tarif.getParDefaut()),
                tarif.getPoidsMax(),
                tarif.getZone() != null ? tarif.getZone().getId() : null);
    }

    public Tarif toEntity(TarifRequest request) {
        return Tarif.builder()
                .libelle(request.getLibelle())
                .prixBase(request.getPrixBase())
                .prixParKm(request.getPrixParKm())
                .prixParKg(request.getPrixParKg())
                .prixParM3(request.getPrixParM3())
                .coefficientZone(request.getCoefficientZone() != null ? request.getCoefficientZone() : BigDecimal.ONE)
                .parDefaut(request.isParDefaut())
                .poidsMax(request.getPoidsMax())
                .build();
    }

    public void apply(TarifRequest request, Tarif tarif) {
        tarif.setLibelle(request.getLibelle());
        tarif.setPrixBase(request.getPrixBase());
        tarif.setPrixParKm(request.getPrixParKm());
        tarif.setPrixParKg(request.getPrixParKg());
        tarif.setPrixParM3(request.getPrixParM3());
        tarif.setCoefficientZone(request.getCoefficientZone() != null ? request.getCoefficientZone() : BigDecimal.ONE);
        tarif.setParDefaut(request.isParDefaut());
        tarif.setPoidsMax(request.getPoidsMax());
    }
}
