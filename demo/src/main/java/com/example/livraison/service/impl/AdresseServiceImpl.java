package com.example.livraison.service.impl;

import com.example.livraison.dto.request.AdresseRequest;
import com.example.livraison.dto.response.AdresseResponse;
import com.example.livraison.entity.Adresse;
import com.example.livraison.entity.Client;
import com.example.livraison.exception.ResourceNotFoundException;
import com.example.livraison.mapper.AdresseMapper;
import com.example.livraison.repository.AdresseRepository;
import com.example.livraison.repository.ClientRepository;
import com.example.livraison.service.AdresseService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdresseServiceImpl implements AdresseService {

    private final AdresseRepository adresseRepository;
    private final ClientRepository clientRepository;
    private final AdresseMapper adresseMapper;

    public AdresseServiceImpl(AdresseRepository adresseRepository, ClientRepository clientRepository,
                              AdresseMapper adresseMapper) {
        this.adresseRepository = adresseRepository;
        this.clientRepository = clientRepository;
        this.adresseMapper = adresseMapper;
    }

    @Override
    public AdresseResponse create(AdresseRequest request) {
        Adresse adresse = adresseMapper.toEntity(request);
        adresse.setClient(resolveClient(request.getClientId()));
        return adresseMapper.toResponse(adresseRepository.save(adresse));
    }

    @Override
    public AdresseResponse update(Long id, AdresseRequest request) {
        Adresse adresse = findAdresse(id);
        adresseMapper.apply(request, adresse);
        adresse.setClient(resolveClient(request.getClientId()));
        return adresseMapper.toResponse(adresseRepository.save(adresse));
    }

    @Override
    @Transactional(readOnly = true)
    public AdresseResponse getById(Long id) {
        return adresseMapper.toResponse(findAdresse(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdresseResponse> getAll() {
        return adresseRepository.findAll().stream().map(adresseMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdresseResponse> getByClient(Long clientId) {
        return adresseRepository.findByClientId(clientId).stream().map(adresseMapper::toResponse).toList();
    }

    @Override
    public void delete(Long id) {
        adresseRepository.delete(findAdresse(id));
    }

    private Adresse findAdresse(Long id) {
        return adresseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Adresse", id));
    }

    private Client resolveClient(Long clientId) {
        if (clientId == null) {
            return null;
        }
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client", clientId));
    }
}
