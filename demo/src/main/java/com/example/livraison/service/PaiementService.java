package com.example.livraison.service;

import com.example.livraison.dto.request.PaiementRequest;
import com.example.livraison.dto.response.PaiementResponse;
import com.example.livraison.entity.StatutPaiement;
import java.util.List;

public interface PaiementService {

    PaiementResponse create(PaiementRequest request);

    PaiementResponse getById(Long id);

    List<PaiementResponse> getAll();

    PaiementResponse getByCommande(Long commandeId);

    PaiementResponse updateStatut(Long id, StatutPaiement statut);
}
