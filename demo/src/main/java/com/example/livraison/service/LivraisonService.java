package com.example.livraison.service;

import com.example.livraison.dto.request.LivraisonRequest;
import com.example.livraison.dto.response.LivraisonResponse;
import com.example.livraison.entity.StatutLivraison;
import java.util.List;

public interface LivraisonService {

    LivraisonResponse create(LivraisonRequest request);

    LivraisonResponse getById(Long id);

    List<LivraisonResponse> getAll();

    List<LivraisonResponse> getByLivreur(Long livreurId);

    LivraisonResponse assignerLivreur(Long livraisonId, Long livreurId);

    LivraisonResponse updateStatut(Long livraisonId, StatutLivraison statut);
}
