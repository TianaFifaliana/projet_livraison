package com.example.livraison.service.impl;

import com.example.livraison.dto.request.CreateCommandeRequest;
import com.example.livraison.dto.request.UpdateCommandeRequest;
import com.example.livraison.dto.response.CommandeResponse;
import com.example.livraison.entity.Client;
import com.example.livraison.entity.Commande;
import com.example.livraison.entity.Livreur;
import com.example.livraison.entity.PreuveLivraison;
import com.example.livraison.entity.Priorite;
import com.example.livraison.entity.StatutCommande;
import com.example.livraison.entity.Zone;
import com.example.livraison.exception.BusinessException;
import com.example.livraison.exception.ResourceNotFoundException;
import com.example.livraison.mapper.CommandeMapper;
import com.example.livraison.repository.ClientRepository;
import com.example.livraison.repository.CommandeRepository;
import com.example.livraison.repository.LivreurRepository;
import com.example.livraison.repository.PreuveLivraisonRepository;
import com.example.livraison.repository.ZoneRepository;
import com.example.livraison.service.CommandeService;
import com.example.livraison.service.PrixService;
import com.example.livraison.util.ReferenceGenerator;
import com.example.livraison.util.StatutTransitionValidator;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommandeServiceImpl implements CommandeService {

    private final CommandeRepository commandeRepository;
    private final ClientRepository clientRepository;
    private final LivreurRepository livreurRepository;
    private final ZoneRepository zoneRepository;
    private final PreuveLivraisonRepository preuveLivraisonRepository;
    private final CommandeMapper commandeMapper;
    private final ReferenceGenerator referenceGenerator;
    private final PrixService prixService;
    private final StatutTransitionValidator statutTransitionValidator;

    public CommandeServiceImpl(CommandeRepository commandeRepository, ClientRepository clientRepository,
                               LivreurRepository livreurRepository, ZoneRepository zoneRepository,
                               PreuveLivraisonRepository preuveLivraisonRepository, CommandeMapper commandeMapper,
                               ReferenceGenerator referenceGenerator, PrixService prixService,
                               StatutTransitionValidator statutTransitionValidator) {
        this.commandeRepository = commandeRepository;
        this.clientRepository = clientRepository;
        this.livreurRepository = livreurRepository;
        this.zoneRepository = zoneRepository;
        this.preuveLivraisonRepository = preuveLivraisonRepository;
        this.commandeMapper = commandeMapper;
        this.referenceGenerator = referenceGenerator;
        this.prixService = prixService;
        this.statutTransitionValidator = statutTransitionValidator;
    }

    @Override
    public CommandeResponse create(CreateCommandeRequest request) {
        Commande commande = commandeMapper.toEntity(request);
        commande.setNumero(genererNumeroUnique());
        commande.setStatut(StatutCommande.CREEE);
        commande.setClient(resolveClient(request.getClientId()));
        commande.setLivreur(resolveLivreur(request.getLivreurId()));
        Zone zone = resolveZone(request.getZoneId());
        commande.setZone(zone);
        commande.setPrix(calculerPrix(commande, zone));
        return commandeMapper.toResponse(commandeRepository.save(commande));
    }

    @Override
    public CommandeResponse update(Long id, UpdateCommandeRequest request) {
        Commande commande = findCommande(id);
        StatutCommande statutActuel = commande.getStatut();
        commandeMapper.apply(request, commande);
        if (request.getStatut() != null && request.getStatut() != statutActuel) {
            statutTransitionValidator.validerTransition(statutActuel, request.getStatut());
        }
        if (request.getClientId() != null) {
            commande.setClient(resolveClient(request.getClientId()));
        }
        commande.setLivreur(resolveLivreur(request.getLivreurId()));
        Zone zone = resolveZone(request.getZoneId());
        commande.setZone(zone);
        commande.setPrix(calculerPrix(commande, zone));
        return commandeMapper.toResponse(commandeRepository.save(commande));
    }

    @Override
    @Transactional(readOnly = true)
    public CommandeResponse getById(Long id) {
        return commandeMapper.toResponse(findCommande(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommandeResponse> getAll() {
        return commandeRepository.findAll().stream().map(commandeMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommandeResponse> getByClient(Long clientId) {
        return commandeRepository.findByClientId(clientId).stream().map(commandeMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommandeResponse> getByStatut(StatutCommande statut) {
        return commandeRepository.findByStatut(statut).stream().map(commandeMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommandeResponse> rechercher(StatutCommande statut, Long clientId, Long livreurId, Priorite priorite,
                                             LocalDateTime dateDebut, LocalDateTime dateFin, Pageable pageable) {
        return commandeRepository
                .rechercher(statut, clientId, livreurId, priorite, dateDebut, dateFin, pageable)
                .map(commandeMapper::toResponse);
    }

    @Override
    public CommandeResponse changerStatut(Long id, StatutCommande statut) {
        Commande commande = findCommande(id);
        if (commande.getStatut() == statut) {
            return commandeMapper.toResponse(commande);
        }
        statutTransitionValidator.validerTransition(commande.getStatut(), statut);
        if (statut == StatutCommande.LIVREE) {
            validerPreuveLivraison(commande);
        }
        commande.setStatut(statut);
        return commandeMapper.toResponse(commandeRepository.save(commande));
    }

    @Override
    public CommandeResponse annuler(Long id) {
        Commande commande = findCommande(id);
        statutTransitionValidator.validerTransition(commande.getStatut(), StatutCommande.ANNULEE);
        commande.setStatut(StatutCommande.ANNULEE);
        return commandeMapper.toResponse(commandeRepository.save(commande));
    }

    @Override
    public CommandeResponse cloturer(Long id) {
        Commande commande = findCommande(id);
        statutTransitionValidator.validerTransition(commande.getStatut(), StatutCommande.LIVREE);
        validerPreuveLivraison(commande);
        commande.setStatut(StatutCommande.LIVREE);
        return commandeMapper.toResponse(commandeRepository.save(commande));
    }

    @Override
    public void delete(Long id) {
        commandeRepository.delete(findCommande(id));
    }

    private void validerPreuveLivraison(Commande commande) {
        PreuveLivraison preuve = preuveLivraisonRepository.findByCommandeId(commande.getId())
                .orElseThrow(() -> new BusinessException(
                        "La commande " + commande.getId() + " ne peut pas etre cloturee sans preuve de livraison."));
        if (preuve.getDateLivraison() == null) {
            throw new BusinessException("La preuve de livraison doit contenir la date et l'heure de livraison.");
        }
        if (preuve.getNomSignataire() == null || preuve.getNomSignataire().isBlank()) {
            throw new BusinessException("La preuve de livraison doit contenir le nom du receptionnaire.");
        }
        if (preuve.getCodeConfirmation() == null || preuve.getCodeConfirmation().isBlank()) {
            throw new BusinessException("La preuve de livraison doit contenir le code de confirmation.");
        }
    }

    private java.math.BigDecimal calculerPrix(Commande commande, Zone zone) {
        return prixService.calculerMontant(
                commande.getDistance(),
                commande.getPoids(),
                commande.getVolume(),
                commande.getPriorite(),
                zone != null ? zone.getId() : null);
    }

    private String genererNumeroUnique() {
        String numero = referenceGenerator.generateCommandeReference();
        while (commandeRepository.existsByNumero(numero)) {
            numero = referenceGenerator.generateCommandeReference();
        }
        return numero;
    }

    private Commande findCommande(Long id) {
        return commandeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Commande", id));
    }

    private Client resolveClient(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client", clientId));
    }

    private Livreur resolveLivreur(Long livreurId) {
        if (livreurId == null) {
            return null;
        }
        return livreurRepository.findById(livreurId)
                .orElseThrow(() -> new ResourceNotFoundException("Livreur", livreurId));
    }

    private Zone resolveZone(Long zoneId) {
        if (zoneId == null) {
            return null;
        }
        return zoneRepository.findById(zoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Zone", zoneId));
    }
}
