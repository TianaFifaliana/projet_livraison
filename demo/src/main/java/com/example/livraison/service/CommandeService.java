package com.example.livraison.service;

import com.example.livraison.dto.request.CommandeRequest;
import com.example.livraison.dto.response.CommandeResponse;
import com.example.livraison.entity.StatutCommande;
import java.util.List;

public interface CommandeService {

    CommandeResponse create(CommandeRequest request);

    CommandeResponse update(Long id, CommandeRequest request);

    CommandeResponse getById(Long id);

    List<CommandeResponse> getAll();

    List<CommandeResponse> getByClient(Long clientId);

    List<CommandeResponse> getByStatut(StatutCommande statut);

    CommandeResponse updateStatut(Long id, StatutCommande statut);

    void delete(Long id);
}
