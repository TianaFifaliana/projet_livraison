package com.example.livraison.service.impl;

import com.example.livraison.dto.request.CreateClientRequest;
import com.example.livraison.dto.response.ClientResponse;
import com.example.livraison.entity.Client;
import com.example.livraison.entity.Utilisateur;
import com.example.livraison.exception.DuplicateResourceException;
import com.example.livraison.exception.ResourceNotFoundException;
import com.example.livraison.mapper.ClientMapper;
import com.example.livraison.repository.ClientRepository;
import com.example.livraison.repository.UtilisateurRepository;
import com.example.livraison.service.ClientService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ClientMapper clientMapper;

    public ClientServiceImpl(ClientRepository clientRepository, UtilisateurRepository utilisateurRepository,
                             ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    public ClientResponse create(CreateClientRequest request) {
        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Un client avec cet email existe deja : " + request.getEmail());
        }
        Client client = clientMapper.toEntity(request);
        client.setUtilisateur(resolveUtilisateur(request.getUtilisateurId()));
        return clientMapper.toResponse(clientRepository.save(client));
    }

    @Override
    public ClientResponse update(Long id, CreateClientRequest request) {
        Client client = findClient(id);
        if (!client.getEmail().equals(request.getEmail()) && clientRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Un client avec cet email existe deja : " + request.getEmail());
        }
        clientMapper.apply(request, client);
        client.setUtilisateur(resolveUtilisateur(request.getUtilisateurId()));
        return clientMapper.toResponse(clientRepository.save(client));
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponse getById(Long id) {
        return clientMapper.toResponse(findClient(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientResponse> getAll() {
        return clientRepository.findAll().stream().map(clientMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClientResponse> rechercher(String recherche, Pageable pageable) {
        return clientRepository.rechercher(recherche, pageable).map(clientMapper::toResponse);
    }

    @Override
    public void delete(Long id) {
        clientRepository.delete(findClient(id));
    }

    private Client findClient(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client", id));
    }

    private Utilisateur resolveUtilisateur(Long utilisateurId) {
        if (utilisateurId == null) {
            return null;
        }
        return utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", utilisateurId));
    }
}
