package com.example.livraison.service.impl;

import com.example.livraison.dto.request.CreateLivreurRequest;
import com.example.livraison.dto.response.LivreurResponse;
import com.example.livraison.entity.Livreur;
import com.example.livraison.entity.StatutLivreur;
import com.example.livraison.entity.Utilisateur;
import com.example.livraison.entity.Vehicule;
import com.example.livraison.entity.Zone;
import com.example.livraison.exception.DuplicateResourceException;
import com.example.livraison.exception.ResourceNotFoundException;
import com.example.livraison.mapper.LivreurMapper;
import com.example.livraison.repository.LivreurRepository;
import com.example.livraison.repository.UtilisateurRepository;
import com.example.livraison.repository.VehiculeRepository;
import com.example.livraison.repository.ZoneRepository;
import com.example.livraison.service.LivreurService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LivreurServiceImpl implements LivreurService {

    private final LivreurRepository livreurRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final VehiculeRepository vehiculeRepository;
    private final ZoneRepository zoneRepository;
    private final LivreurMapper livreurMapper;

    public LivreurServiceImpl(LivreurRepository livreurRepository, UtilisateurRepository utilisateurRepository,
                              VehiculeRepository vehiculeRepository, ZoneRepository zoneRepository,
                              LivreurMapper livreurMapper) {
        this.livreurRepository = livreurRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.zoneRepository = zoneRepository;
        this.livreurMapper = livreurMapper;
    }

    @Override
    public LivreurResponse create(CreateLivreurRequest request) {
        if (livreurRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Un livreur avec cet email existe deja : " + request.getEmail());
        }
        Livreur livreur = livreurMapper.toEntity(request);
        applyRelations(request, livreur);
        return livreurMapper.toResponse(livreurRepository.save(livreur));
    }

    @Override
    public LivreurResponse update(Long id, CreateLivreurRequest request) {
        Livreur livreur = findLivreur(id);
        if (!livreur.getEmail().equals(request.getEmail()) && livreurRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Un livreur avec cet email existe deja : " + request.getEmail());
        }
        livreurMapper.apply(request, livreur);
        applyRelations(request, livreur);
        return livreurMapper.toResponse(livreurRepository.save(livreur));
    }

    @Override
    @Transactional(readOnly = true)
    public LivreurResponse getById(Long id) {
        return livreurMapper.toResponse(findLivreur(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LivreurResponse> getAll() {
        return livreurRepository.findAll().stream().map(livreurMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LivreurResponse> getDisponibles() {
        return livreurRepository.findByStatut(StatutLivreur.DISPONIBLE).stream()
                .map(livreurMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LivreurResponse> rechercher(String recherche, StatutLivreur statut, Long zoneId, Pageable pageable) {
        return livreurRepository.rechercher(recherche, statut, zoneId, pageable).map(livreurMapper::toResponse);
    }

    @Override
    public LivreurResponse changerStatut(Long id, StatutLivreur statut) {
        Livreur livreur = findLivreur(id);
        livreur.setStatut(statut);
        return livreurMapper.toResponse(livreurRepository.save(livreur));
    }

    @Override
    public void delete(Long id) {
        livreurRepository.delete(findLivreur(id));
    }

    private void applyRelations(CreateLivreurRequest request, Livreur livreur) {
        livreur.setUtilisateur(resolveUtilisateur(request.getUtilisateurId()));
        livreur.setZone(resolveZone(request.getZoneId()));
        livreur.setVehicule(resolveVehicule(request.getVehiculeId()));
    }

    private Livreur findLivreur(Long id) {
        return livreurRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Livreur", id));
    }

    private Utilisateur resolveUtilisateur(Long utilisateurId) {
        if (utilisateurId == null) {
            return null;
        }
        return utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", utilisateurId));
    }

    private Vehicule resolveVehicule(Long vehiculeId) {
        if (vehiculeId == null) {
            return null;
        }
        return vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", vehiculeId));
    }

    private Zone resolveZone(Long zoneId) {
        if (zoneId == null) {
            return null;
        }
        return zoneRepository.findById(zoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Zone", zoneId));
    }
}
