package com.example.livraison.service.impl;

import com.example.livraison.dto.request.CreateIncidentRequest;
import com.example.livraison.dto.response.IncidentResponse;
import com.example.livraison.entity.Commande;
import com.example.livraison.entity.Incident;
import com.example.livraison.entity.Livreur;
import com.example.livraison.exception.BusinessException;
import com.example.livraison.exception.ResourceNotFoundException;
import com.example.livraison.mapper.IncidentMapper;
import com.example.livraison.repository.CommandeRepository;
import com.example.livraison.repository.IncidentRepository;
import com.example.livraison.repository.LivreurRepository;
import com.example.livraison.service.IncidentService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;
    private final CommandeRepository commandeRepository;
    private final LivreurRepository livreurRepository;
    private final IncidentMapper incidentMapper;

    public IncidentServiceImpl(IncidentRepository incidentRepository, CommandeRepository commandeRepository,
                               LivreurRepository livreurRepository, IncidentMapper incidentMapper) {
        this.incidentRepository = incidentRepository;
        this.commandeRepository = commandeRepository;
        this.livreurRepository = livreurRepository;
        this.incidentMapper = incidentMapper;
    }

    @Override
    public IncidentResponse signaler(CreateIncidentRequest request) {
        Incident incident = incidentMapper.toEntity(request);
        incident.setCommande(resolveCommande(request.getCommandeId()));
        incident.setLivreur(resolveLivreur(request.getLivreurId()));
        incident.setResolu(false);
        return incidentMapper.toResponse(incidentRepository.save(incident));
    }

    @Override
    public IncidentResponse resoudre(Long id, String commentaireResolution) {
        Incident incident = findIncident(id);
        if (Boolean.TRUE.equals(incident.getResolu())) {
            throw new BusinessException("L'incident " + id + " est deja resolu.");
        }
        incident.setResolu(true);
        incident.setDateResolution(LocalDateTime.now());
        incident.setCommentaireResolution(commentaireResolution);
        return incidentMapper.toResponse(incidentRepository.save(incident));
    }

    @Override
    @Transactional(readOnly = true)
    public IncidentResponse getById(Long id) {
        return incidentMapper.toResponse(findIncident(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncidentResponse> getAll() {
        return incidentRepository.findAll().stream().map(incidentMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncidentResponse> getByCommande(Long commandeId) {
        return incidentRepository.findByCommandeId(commandeId).stream().map(incidentMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncidentResponse> getByLivreur(Long livreurId) {
        return incidentRepository.findByLivreurId(livreurId).stream().map(incidentMapper::toResponse).toList();
    }

    private Incident findIncident(Long id) {
        return incidentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Incident", id));
    }

    private Commande resolveCommande(Long commandeId) {
        return commandeRepository.findById(commandeId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande", commandeId));
    }

    private Livreur resolveLivreur(Long livreurId) {
        return livreurRepository.findById(livreurId)
                .orElseThrow(() -> new ResourceNotFoundException("Livreur", livreurId));
    }
}
