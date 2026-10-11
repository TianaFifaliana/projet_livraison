package com.example.livraison.service;

import com.example.livraison.dto.request.TarifRequest;
import com.example.livraison.dto.response.TarifResponse;
import java.util.List;

public interface TarifService {

    TarifResponse create(TarifRequest request);

    TarifResponse update(Long id, TarifRequest request);

    TarifResponse getById(Long id);

    List<TarifResponse> getAll();

    List<TarifResponse> getByZone(Long zoneId);

    TarifResponse getParDefaut();

    void delete(Long id);
}
