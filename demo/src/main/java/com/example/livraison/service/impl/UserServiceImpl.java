package com.example.livraison.service.impl;

import com.example.livraison.dto.request.UserRequest;
import com.example.livraison.dto.response.UserResponse;
import com.example.livraison.entity.Utilisateur;
import com.example.livraison.exception.DuplicateResourceException;
import com.example.livraison.exception.ResourceNotFoundException;
import com.example.livraison.mapper.UserMapper;
import com.example.livraison.repository.UtilisateurRepository;
import com.example.livraison.service.UserService;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UtilisateurRepository utilisateurRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UtilisateurRepository utilisateurRepository,
                           UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse create(UserRequest request) {
        if (utilisateurRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Le nom d'utilisateur existe deja : " + request.getUsername());
        }
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("L'email existe deja : " + request.getEmail());
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new com.example.livraison.exception.BusinessException("Le mot de passe est obligatoire");
        }
        Utilisateur utilisateur = userMapper.toEntity(request);
        utilisateur.setPassword(passwordEncoder.encode(request.getPassword()));
        return userMapper.toResponse(utilisateurRepository.save(utilisateur));
    }

    @Override
    public UserResponse update(Long id, UserRequest request) {
        Utilisateur utilisateur = findUtilisateur(id);
        if (!utilisateur.getUsername().equals(request.getUsername())
                && utilisateurRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Le nom d'utilisateur existe deja : " + request.getUsername());
        }
        if (!utilisateur.getEmail().equals(request.getEmail())
                && utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("L'email existe deja : " + request.getEmail());
        }
        userMapper.apply(request, utilisateur);
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            utilisateur.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        return userMapper.toResponse(utilisateurRepository.save(utilisateur));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        return userMapper.toResponse(findUtilisateur(id));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getByUsername(String username) {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + username));
        return userMapper.toResponse(utilisateur);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAll() {
        return utilisateurRepository.findAll().stream().map(userMapper::toResponse).toList();
    }

    @Override
    public void delete(Long id) {
        utilisateurRepository.delete(findUtilisateur(id));
    }

    @Override
    public void changePassword(Long id, String newPassword) {
        Utilisateur utilisateur = findUtilisateur(id);
        utilisateur.setPassword(passwordEncoder.encode(newPassword));
        utilisateurRepository.save(utilisateur);
    }

    private Utilisateur findUtilisateur(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }
}