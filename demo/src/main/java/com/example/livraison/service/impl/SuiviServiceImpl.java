package com.example.livraison.service.impl;

import com.example.livraison.dto.request.SuiviRequest;
import com.example.livraison.dto.response.SuiviResponse;
import com.example.livraison.entity.Livraison;
import com.example.livraison.entity.Suivi;
import com.example.livraison.exception.ResourceNotFoundException;
import com.example.livraison.mapper.SuiviMapper;
import com.example.livraison.repository.LivraisonRepository;
import com.example.livraison.repository.SuiviRepository;
import com.example.livraison.service.SuiviService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SuiviServiceImpl implements SuiviService {

    private final SuiviRepository suiviRepository;
    private final LivraisonRepository livraisonRepository;
    private final SuiviMapper suiviMapper;

    public SuiviServiceImpl(SuiviRepository suiviRepository, LivraisonRepository livraisonRepository,
                            SuiviMapper suiviMapper) {
        this.suiviRepository = suiviRepository;
        this.livraisonRepository = livraisonRepository;
        this.suiviMapper = suiviMapper;
    }

    @Override
    public SuiviResponse add(SuiviRequest request) {
        Livraison livraison = livraisonRepository.findById(request.getLivraisonId())
                .orElseThrow(() -> new ResourceNotFoundException("Livraison", request.getLivraisonId()));
        Suivi suivi = suiviMapper.toEntity(request);
        suivi.setLivraison(livraison);
        return suiviMapper.toResponse(suiviRepository.save(suivi));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SuiviResponse> getByLivraison(Long livraisonId) {
        return suiviRepository.findByLivraisonIdOrderByDateAsc(livraisonId).stream()
                .map(suiviMapper::toResponse)
                .toList();
    }
}
