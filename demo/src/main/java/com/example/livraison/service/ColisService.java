package com.example.livraison.service;

import com.example.livraison.dto.request.ColisRequest;
import com.example.livraison.dto.response.ColisResponse;
import java.util.List;

public interface ColisService {

    ColisResponse create(ColisRequest request);

    ColisResponse update(Long id, ColisRequest request);

    ColisResponse getById(Long id);

    List<ColisResponse> getAll();

    List<ColisResponse> getByCommande(Long commandeId);

    void delete(Long id);
}
