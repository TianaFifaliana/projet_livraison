package com.example.livraison.service;

import com.example.livraison.dto.request.CreateIncidentRequest;
import com.example.livraison.dto.response.IncidentResponse;
import java.util.List;

public interface IncidentService {

    IncidentResponse signaler(CreateIncidentRequest request);

    IncidentResponse resoudre(Long id, String commentaireResolution);

    IncidentResponse getById(Long id);

    List<IncidentResponse> getAll();

    List<IncidentResponse> getByCommande(Long commandeId);

    List<IncidentResponse> getByLivreur(Long livreurId);
}
