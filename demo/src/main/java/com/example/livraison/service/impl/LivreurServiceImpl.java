package com.example.livraison.service.impl;

import com.example.livraison.dto.request.LivreurRequest;
import com.example.livraison.dto.response.LivreurResponse;
import com.example.livraison.entity.Livreur;
import com.example.livraison.entity.User;
import com.example.livraison.exception.DuplicateResourceException;
import com.example.livraison.exception.ResourceNotFoundException;
import com.example.livraison.mapper.LivreurMapper;
import com.example.livraison.repository.LivreurRepository;
import com.example.livraison.repository.UserRepository;
import com.example.livraison.service.LivreurService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LivreurServiceImpl implements LivreurService {

    private final LivreurRepository livreurRepository;
    private final UserRepository userRepository;
    private final LivreurMapper livreurMapper;

    public LivreurServiceImpl(LivreurRepository livreurRepository, UserRepository userRepository,
                              LivreurMapper livreurMapper) {
        this.livreurRepository = livreurRepository;
        this.userRepository = userRepository;
        this.livreurMapper = livreurMapper;
    }

    @Override
    public LivreurResponse create(LivreurRequest request) {
        if (livreurRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Un livreur avec cet email existe deja : " + request.getEmail());
        }
        Livreur livreur = livreurMapper.toEntity(request);
        livreur.setUser(resolveUser(request.getUserId()));
        return livreurMapper.toResponse(livreurRepository.save(livreur));
    }

    @Override
    public LivreurResponse update(Long id, LivreurRequest request) {
        Livreur livreur = findLivreur(id);
        if (!livreur.getEmail().equals(request.getEmail()) && livreurRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Un livreur avec cet email existe deja : " + request.getEmail());
        }
        livreurMapper.apply(request, livreur);
        livreur.setUser(resolveUser(request.getUserId()));
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
        return livreurRepository.findByDisponibleTrue().stream().map(livreurMapper::toResponse).toList();
    }

    @Override
    public void delete(Long id) {
        Livreur livreur = findLivreur(id);
        livreurRepository.delete(livreur);
    }

    private Livreur findLivreur(Long id) {
        return livreurRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Livreur", id));
    }

    private User resolveUser(Long userId) {
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }
}
