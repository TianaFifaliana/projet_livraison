package com.example.livraison.service.impl;

import com.example.livraison.dto.request.ColisRequest;
import com.example.livraison.dto.response.ColisResponse;
import com.example.livraison.entity.Colis;
import com.example.livraison.entity.Commande;
import com.example.livraison.exception.ResourceNotFoundException;
import com.example.livraison.mapper.ColisMapper;
import com.example.livraison.repository.ColisRepository;
import com.example.livraison.repository.CommandeRepository;
import com.example.livraison.service.ColisService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ColisServiceImpl implements ColisService {

    private final ColisRepository colisRepository;
    private final CommandeRepository commandeRepository;
    private final ColisMapper colisMapper;

    public ColisServiceImpl(ColisRepository colisRepository, CommandeRepository commandeRepository,
                            ColisMapper colisMapper) {
        this.colisRepository = colisRepository;
        this.commandeRepository = commandeRepository;
        this.colisMapper = colisMapper;
    }

    @Override
    public ColisResponse create(ColisRequest request) {
        if (request.getCommandeId() == null) {
            throw new com.example.livraison.exception.BusinessException("La commande est obligatoire");
        }
        Colis colis = colisMapper.toEntity(request);
        colis.setCommande(resolveCommande(request.getCommandeId()));
        return colisMapper.toResponse(colisRepository.save(colis));
    }

    @Override
    public ColisResponse update(Long id, ColisRequest request) {
        Colis colis = findColis(id);
        colisMapper.apply(request, colis);
        if (request.getCommandeId() != null) {
            colis.setCommande(resolveCommande(request.getCommandeId()));
        }
        return colisMapper.toResponse(colisRepository.save(colis));
    }

    @Override
    @Transactional(readOnly = true)
    public ColisResponse getById(Long id) {
        return colisMapper.toResponse(findColis(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ColisResponse> getAll() {
        return colisRepository.findAll().stream().map(colisMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ColisResponse> getByCommande(Long commandeId) {
        return colisRepository.findByCommandeId(commandeId).stream().map(colisMapper::toResponse).toList();
    }

    @Override
    public void delete(Long id) {
        colisRepository.delete(findColis(id));
    }

    private Colis findColis(Long id) {
        return colisRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Colis", id));
    }

    private Commande resolveCommande(Long commandeId) {
        return commandeRepository.findById(commandeId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande", commandeId));
    }
}
