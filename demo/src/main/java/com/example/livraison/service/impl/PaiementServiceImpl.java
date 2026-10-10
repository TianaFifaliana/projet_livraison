package com.example.livraison.service.impl;

import com.example.livraison.dto.request.PaiementRequest;
import com.example.livraison.dto.response.PaiementResponse;
import com.example.livraison.entity.Commande;
import com.example.livraison.entity.Paiement;
import com.example.livraison.entity.StatutPaiement;
import com.example.livraison.exception.BusinessException;
import com.example.livraison.exception.ResourceNotFoundException;
import com.example.livraison.mapper.PaiementMapper;
import com.example.livraison.repository.CommandeRepository;
import com.example.livraison.repository.PaiementRepository;
import com.example.livraison.service.PaiementService;
import com.example.livraison.util.ReferenceGenerator;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaiementServiceImpl implements PaiementService {

    private final PaiementRepository paiementRepository;
    private final CommandeRepository commandeRepository;
    private final PaiementMapper paiementMapper;
    private final ReferenceGenerator referenceGenerator;

    public PaiementServiceImpl(PaiementRepository paiementRepository, CommandeRepository commandeRepository,
                               PaiementMapper paiementMapper, ReferenceGenerator referenceGenerator) {
        this.paiementRepository = paiementRepository;
        this.commandeRepository = commandeRepository;
        this.paiementMapper = paiementMapper;
        this.referenceGenerator = referenceGenerator;
    }

    @Override
    public PaiementResponse create(PaiementRequest request) {
        Commande commande = commandeRepository.findById(request.getCommandeId())
                .orElseThrow(() -> new ResourceNotFoundException("Commande", request.getCommandeId()));
        if (paiementRepository.findByCommandeId(commande.getId()).isPresent()) {
            throw new BusinessException("Un paiement existe deja pour cette commande");
        }
        Paiement paiement = paiementMapper.toEntity(request);
        paiement.setCommande(commande);
        if (paiement.getTransactionId() == null || paiement.getTransactionId().isBlank()) {
            paiement.setTransactionId(referenceGenerator.generateTransactionId());
        }
        if (paiement.getStatut() == StatutPaiement.PAYE && paiement.getDatePaiement() == null) {
            paiement.setDatePaiement(LocalDateTime.now());
        }
        return paiementMapper.toResponse(paiementRepository.save(paiement));
    }

    @Override
    @Transactional(readOnly = true)
    public PaiementResponse getById(Long id) {
        return paiementMapper.toResponse(findPaiement(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaiementResponse> getAll() {
        return paiementRepository.findAll().stream().map(paiementMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PaiementResponse getByCommande(Long commandeId) {
        Paiement paiement = paiementRepository.findByCommandeId(commandeId)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement introuvable pour la commande " + commandeId));
        return paiementMapper.toResponse(paiement);
    }

    @Override
    public PaiementResponse updateStatut(Long id, StatutPaiement statut) {
        Paiement paiement = findPaiement(id);
        paiement.setStatut(statut);
        if (statut == StatutPaiement.PAYE && paiement.getDatePaiement() == null) {
            paiement.setDatePaiement(LocalDateTime.now());
        }
        return paiementMapper.toResponse(paiementRepository.save(paiement));
    }

    private Paiement findPaiement(Long id) {
        return paiementRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Paiement", id));
    }
}
