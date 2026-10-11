package com.example.livraison.service;

import com.example.livraison.dto.request.CreateLivreurRequest;
import com.example.livraison.dto.response.LivreurResponse;
import com.example.livraison.entity.StatutLivreur;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LivreurService {

    LivreurResponse create(CreateLivreurRequest request);

    LivreurResponse update(Long id, CreateLivreurRequest request);

    LivreurResponse getById(Long id);

    List<LivreurResponse> getAll();

    List<LivreurResponse> getDisponibles();

    Page<LivreurResponse> rechercher(String recherche, StatutLivreur statut, Long zoneId, Pageable pageable);

    LivreurResponse changerStatut(Long id, StatutLivreur statut);

    void delete(Long id);
}
