package com.example.livraison.service.impl;

import com.example.livraison.dto.request.CalculPrixRequest;
import com.example.livraison.dto.response.PrixResponse;
import com.example.livraison.entity.Priorite;
import com.example.livraison.entity.Tarif;
import com.example.livraison.repository.TarifRepository;
import com.example.livraison.service.PrixService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PrixServiceImpl implements PrixService {

    private static final BigDecimal PRIX_BASE_DEFAUT = new BigDecimal("5.00");
    private static final BigDecimal PRIX_PAR_KM_DEFAUT = new BigDecimal("0.50");
    private static final BigDecimal PRIX_PAR_KG_DEFAUT = new BigDecimal("0.20");
    private static final BigDecimal PRIX_PAR_M3_DEFAUT = new BigDecimal("1.00");
    private static final BigDecimal COEFFICIENT_DEFAUT = BigDecimal.ONE;

    private final TarifRepository tarifRepository;

    public PrixServiceImpl(TarifRepository tarifRepository) {
        this.tarifRepository = tarifRepository;
    }

    @Override
    public PrixResponse calculer(CalculPrixRequest request) {
        Priorite priorite = request.getPriorite() != null ? request.getPriorite() : Priorite.NORMALE;
        Tarif tarif = resolveTarif(request.getZoneId());
        boolean tarifParDefaut = tarif == null || Boolean.TRUE.equals(tarif.getParDefaut());

        BigDecimal prixBase = tarif != null && tarif.getPrixBase() != null ? tarif.getPrixBase() : PRIX_BASE_DEFAUT;
        BigDecimal prixParKm = tarif != null && tarif.getPrixParKm() != null ? tarif.getPrixParKm() : PRIX_PAR_KM_DEFAUT;
        BigDecimal prixParKg = tarif != null && tarif.getPrixParKg() != null ? tarif.getPrixParKg() : PRIX_PAR_KG_DEFAUT;
        BigDecimal prixParM3 = tarif != null && tarif.getPrixParM3() != null ? tarif.getPrixParM3() : PRIX_PAR_M3_DEFAUT;
        BigDecimal coefficientZone = tarif != null && tarif.getCoefficientZone() != null
                ? tarif.getCoefficientZone()
                : COEFFICIENT_DEFAUT;
        BigDecimal coefficientPriorite = priorite.getCoefficient();

        BigDecimal montant = prixBase
                .add(valeur(request.getDistance()).multiply(prixParKm))
                .add(valeur(request.getPoids()).multiply(prixParKg))
                .add(valeur(request.getVolume()).multiply(prixParM3))
                .multiply(coefficientPriorite)
                .multiply(coefficientZone)
                .setScale(2, RoundingMode.HALF_UP);

        return new PrixResponse(
                montant,
                prixBase,
                prixParKm,
                prixParKg,
                prixParM3,
                coefficientPriorite,
                coefficientZone,
                tarif != null ? tarif.getId() : null,
                tarif != null ? tarif.getLibelle() : "Tarif par defaut",
                tarifParDefaut);
    }

    @Override
    public BigDecimal calculerMontant(Double distance, Double poids, Double volume, Priorite priorite, Long zoneId) {
        return calculer(CalculPrixRequest.builder()
                .distance(distance)
                .poids(poids)
                .volume(volume)
                .priorite(priorite)
                .zoneId(zoneId)
                .build()).montant();
    }

    private Tarif resolveTarif(Long zoneId) {
        if (zoneId != null) {
            Optional<Tarif> tarifZone = tarifRepository.findFirstByZoneIdOrderByIdAsc(zoneId);
            if (tarifZone.isPresent()) {
                return tarifZone.get();
            }
        }
        return tarifRepository.findFirstByParDefautTrueOrderByIdAsc().orElse(null);
    }

    private BigDecimal valeur(Double montant) {
        return montant == null ? BigDecimal.ZERO : BigDecimal.valueOf(montant);
    }
}
