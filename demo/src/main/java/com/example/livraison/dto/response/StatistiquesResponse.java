package com.example.livraison.dto.response;

import java.math.BigDecimal;
import java.util.Map;

public record StatistiquesResponse(
        long nombreLivraisons,
        double tauxReussite,
        double tauxEchec,
        double delaiMoyenHeures,
        BigDecimal chiffreAffaires,
        Map<Long, Long> livraisonsParLivreur,
        BigDecimal revenuMoyen,
        double distanceTotale) {
}
