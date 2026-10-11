package com.example.livraison.service.impl;

import com.example.livraison.dto.request.TarifRequest;
import com.example.livraison.dto.response.TarifResponse;
import com.example.livraison.entity.Tarif;
import com.example.livraison.entity.Zone;
import com.example.livraison.exception.ResourceNotFoundException;
import com.example.livraison.mapper.TarifMapper;
import com.example.livraison.repository.TarifRepository;
import com.example.livraison.repository.ZoneRepository;
import com.example.livraison.service.TarifService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TarifServiceImpl implements TarifService {

    private final TarifRepository tarifRepository;
    private final ZoneRepository zoneRepository;
    private final TarifMapper tarifMapper;

    public TarifServiceImpl(TarifRepository tarifRepository, ZoneRepository zoneRepository,
                            TarifMapper tarifMapper) {
        this.tarifRepository = tarifRepository;
        this.zoneRepository = zoneRepository;
        this.tarifMapper = tarifMapper;
    }

    @Override
    public TarifResponse create(TarifRequest request) {
        Tarif tarif = tarifMapper.toEntity(request);
        tarif.setZone(resolveZone(request.getZoneId()));
        appliquerTarifParDefaut(tarif);
        return tarifMapper.toResponse(tarifRepository.save(tarif));
    }

    @Override
    public TarifResponse update(Long id, TarifRequest request) {
        Tarif tarif = findTarif(id);
        tarifMapper.apply(request, tarif);
        tarif.setZone(resolveZone(request.getZoneId()));
        appliquerTarifParDefaut(tarif);
        return tarifMapper.toResponse(tarifRepository.save(tarif));
    }

    @Override
    @Transactional(readOnly = true)
    public TarifResponse getById(Long id) {
        return tarifMapper.toResponse(findTarif(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TarifResponse> getAll() {
        return tarifRepository.findAll().stream().map(tarifMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TarifResponse> getByZone(Long zoneId) {
        verifyZoneExists(zoneId);
        return tarifRepository.findByZoneId(zoneId).stream().map(tarifMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TarifResponse getParDefaut() {
        return tarifRepository.findFirstByParDefautTrueOrderByIdAsc()
                .map(tarifMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Aucun tarif par defaut n'est configure"));
    }

    @Override
    public void delete(Long id) {
        tarifRepository.delete(findTarif(id));
    }

    private void appliquerTarifParDefaut(Tarif tarif) {
        if (!Boolean.TRUE.equals(tarif.getParDefaut())) {
            return;
        }
        tarifRepository.findByParDefautTrue().stream()
                .filter(existant -> !existant.equals(tarif))
                .forEach(existant -> existant.setParDefaut(false));
    }

    private Tarif findTarif(Long id) {
        return tarifRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tarif", id));
    }

    private Zone verifyZoneExists(Long zoneId) {
        return zoneRepository.findById(zoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Zone", zoneId));
    }

    private Zone resolveZone(Long zoneId) {
        if (zoneId == null) {
            return null;
        }
        return verifyZoneExists(zoneId);
    }
}
