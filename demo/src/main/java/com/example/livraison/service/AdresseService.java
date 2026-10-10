package com.example.livraison.service;

import com.example.livraison.dto.request.AdresseRequest;
import com.example.livraison.dto.response.AdresseResponse;
import java.util.List;

public interface AdresseService {

    AdresseResponse create(AdresseRequest request);

    AdresseResponse update(Long id, AdresseRequest request);

    AdresseResponse getById(Long id);

    List<AdresseResponse> getAll();

    List<AdresseResponse> getByClient(Long clientId);

    void delete(Long id);
}
