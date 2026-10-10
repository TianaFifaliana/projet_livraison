package com.example.livraison.service.impl;

import com.example.livraison.dto.request.LivraisonRequest;
import com.example.livraison.dto.response.LivraisonResponse;
import com.example.livraison.entity.Commande;
import com.example.livraison.entity.Livraison;
import com.example.livraison.entity.Livreur;
import com.example.livraison.entity.StatutCommande;
import com.example.livraison.entity.StatutLivraison;
import com.example.livraison.entity.Suivi;
import com.example.livraison.exception.BusinessException;
import com.example.livraison.exception.ResourceNotFoundException;
import com.example.livraison.mapper.LivraisonMapper;
import com.example.livraison.repository.CommandeRepository;
import com.example.livraison.repository.LivraisonRepository;
import com.example.livraison.repository.LivreurRepository;
import com.example.livraison.service.LivraisonService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LivraisonServiceImpl implements LivraisonService {

    private final LivraisonRepository livraisonRepository;
    private final CommandeRepository commandeRepository;
    private final LivreurRepository livreurRepository;
    private final LivraisonMapper livraisonMapper;

    public LivraisonServiceImpl(LivraisonRepository livraisonRepository, CommandeRepository commandeRepository,
                                LivreurRepository livreurRepository, LivraisonMapper livraisonMapper) {
        this.livraisonRepository = livraisonRepository;
        this.commandeRepository = commandeRepository;
        this.livreurRepository = livreurRepository;
        this.livraisonMapper = livraisonMapper;
    }

    @Override
    public LivraisonResponse create(LivraisonRequest request) {
        Commande commande = resolveCommande(request.getCommandeId());
        if (livraisonRepository.findByCommandeId(commande.getId()).isPresent()) {
            throw new BusinessException("Une livraison existe deja pour cette commande");
        }
        Livraison livraison = Livraison.builder()
                .commande(commande)
                .livreur(resolveLivreur(request.getLivreurId()))
                .dateLivraisonPrevue(request.getDateLivraisonPrevue())
                .statut(request.getStatut())
                .build();
        Suivi suivi = Suivi.builder()
                .livraison(livraison)
                .statut(request.getStatut())
                .commentaire("Livraison creee")
                .build();
        livraison.getSuivis().add(suivi);
        return livraisonMapper.toResponse(livraisonRepository.save(livraison));
    }

    @Override
    @Transactional(readOnly = true)
    public LivraisonResponse getById(Long id) {
        return livraisonMapper.toResponse(findLivraison(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LivraisonResponse> getAll() {
        return livraisonRepository.findAll().stream().map(livraisonMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LivraisonResponse> getByLivreur(Long livreurId) {
        return livraisonRepository.findByLivreurId(livreurId).stream().map(livraisonMapper::toResponse).toList();
    }

    @Override
    public LivraisonResponse assignerLivreur(Long livraisonId, Long livreurId) {
        Livraison livraison = findLivraison(livraisonId);
        Livreur livreur = resolveLivreur(livreurId);
        livraison.setLivreur(livreur);
        livraison.setStatut(StatutLivraison.ASSIGNEE);
        livraison.getSuivis().add(Suivi.builder()
                .livraison(livraison)
                .statut(StatutLivraison.ASSIGNEE)
                .commentaire("Livreur assigne : " + livreur.getPrenom() + " " + livreur.getNom())
                .build());
        return livraisonMapper.toResponse(livraisonRepository.save(livraison));
    }

    @Override
    public LivraisonResponse updateStatut(Long livraisonId, StatutLivraison statut) {
        Livraison livraison = findLivraison(livraisonId);
        livraison.setStatut(statut);
        if (statut == StatutLivraison.LIVREE) {
            livraison.setDateLivraisonReelle(LocalDateTime.now());
            Commande commande = livraison.getCommande();
            commande.setStatut(StatutCommande.LIVREE);
        }
        livraison.getSuivis().add(Suivi.builder()
                .livraison(livraison)
                .statut(statut)
                .commentaire("Statut mis a jour : " + statut.name())
                .build());
        return livraisonMapper.toResponse(livraisonRepository.save(livraison));
    }

    private Livraison findLivraison(Long id) {
        return livraisonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livraison", id));
    }

    private Commande resolveCommande(Long commandeId) {
        return commandeRepository.findById(commandeId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande", commandeId));
    }

    private Livreur resolveLivreur(Long livreurId) {
        if (livreurId == null) {
            return null;
        }
        return livreurRepository.findById(livreurId)
                .orElseThrow(() -> new ResourceNotFoundException("Livreur", livreurId));
    }
}
