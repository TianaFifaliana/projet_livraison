package com.example.livraison.service.impl;

import com.example.livraison.dto.request.ColisRequest;
import com.example.livraison.dto.request.CommandeRequest;
import com.example.livraison.dto.response.CommandeResponse;
import com.example.livraison.entity.Adresse;
import com.example.livraison.entity.Client;
import com.example.livraison.entity.Colis;
import com.example.livraison.entity.Commande;
import com.example.livraison.entity.StatutCommande;
import com.example.livraison.exception.ResourceNotFoundException;
import com.example.livraison.mapper.ColisMapper;
import com.example.livraison.mapper.CommandeMapper;
import com.example.livraison.repository.AdresseRepository;
import com.example.livraison.repository.ClientRepository;
import com.example.livraison.repository.CommandeRepository;
import com.example.livraison.service.CommandeService;
import com.example.livraison.util.ReferenceGenerator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommandeServiceImpl implements CommandeService {

    private final CommandeRepository commandeRepository;
    private final ClientRepository clientRepository;
    private final AdresseRepository adresseRepository;
    private final CommandeMapper commandeMapper;
    private final ColisMapper colisMapper;
    private final ReferenceGenerator referenceGenerator;

    public CommandeServiceImpl(CommandeRepository commandeRepository, ClientRepository clientRepository,
                               AdresseRepository adresseRepository, CommandeMapper commandeMapper,
                               ColisMapper colisMapper, ReferenceGenerator referenceGenerator) {
        this.commandeRepository = commandeRepository;
        this.clientRepository = clientRepository;
        this.adresseRepository = adresseRepository;
        this.commandeMapper = commandeMapper;
        this.colisMapper = colisMapper;
        this.referenceGenerator = referenceGenerator;
    }

    @Override
    public CommandeResponse create(CommandeRequest request) {
        Commande commande = commandeMapper.toEntity(request);
        commande.setReference(generateUniqueReference());
        commande.setClient(resolveClient(request.getClientId()));
        commande.setAdresseLivraison(resolveAdresse(request.getAdresseId()));
        applyColis(request.getColis(), commande);
        return commandeMapper.toResponse(commandeRepository.save(commande));
    }

    @Override
    public CommandeResponse update(Long id, CommandeRequest request) {
        Commande commande = findCommande(id);
        commandeMapper.apply(request, commande);
        commande.setClient(resolveClient(request.getClientId()));
        commande.setAdresseLivraison(resolveAdresse(request.getAdresseId()));
        commande.getColis().clear();
        applyColis(request.getColis(), commande);
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
    public CommandeResponse updateStatut(Long id, StatutCommande statut) {
        Commande commande = findCommande(id);
        commande.setStatut(statut);
        return commandeMapper.toResponse(commandeRepository.save(commande));
    }

    @Override
    public void delete(Long id) {
        commandeRepository.delete(findCommande(id));
    }

    private void applyColis(List<ColisRequest> requests, Commande commande) {
        if (requests == null) {
            return;
        }
        for (ColisRequest colisRequest : requests) {
            Colis colis = colisMapper.toEntity(colisRequest);
            colis.setCommande(commande);
            commande.getColis().add(colis);
        }
    }

    private String generateUniqueReference() {
        String reference = referenceGenerator.generateCommandeReference();
        while (commandeRepository.existsByReference(reference)) {
            reference = referenceGenerator.generateCommandeReference();
        }
        return reference;
    }

    private Commande findCommande(Long id) {
        return commandeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Commande", id));
    }

    private Client resolveClient(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client", clientId));
    }

    private Adresse resolveAdresse(Long adresseId) {
        return adresseRepository.findById(adresseId)
                .orElseThrow(() -> new ResourceNotFoundException("Adresse", adresseId));
    }
}
