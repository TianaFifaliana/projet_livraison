package com.example.livraison.service;

import com.example.livraison.dto.request.LivreurRequest;
import com.example.livraison.dto.response.LivreurResponse;
import java.util.List;

public interface LivreurService {

    LivreurResponse create(LivreurRequest request);

    LivreurResponse update(Long id, LivreurRequest request);

    LivreurResponse getById(Long id);

    List<LivreurResponse> getAll();

    List<LivreurResponse> getDisponibles();

    void delete(Long id);
}
