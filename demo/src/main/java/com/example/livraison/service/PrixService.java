package com.example.livraison.service;

import com.example.livraison.dto.request.CalculPrixRequest;
import com.example.livraison.dto.response.PrixResponse;
import com.example.livraison.entity.Priorite;
import java.math.BigDecimal;

public interface PrixService {

    PrixResponse calculer(CalculPrixRequest request);

    BigDecimal calculerMontant(Double distance, Double poids, Double volume, Priorite priorite, Long zoneId);
}
