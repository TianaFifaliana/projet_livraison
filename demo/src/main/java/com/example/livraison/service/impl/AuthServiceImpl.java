package com.example.livraison.service.impl;

import com.example.livraison.dto.request.LoginRequest;
import com.example.livraison.dto.request.RegisterRequest;
import com.example.livraison.dto.response.AuthResponse;
import com.example.livraison.dto.response.UserResponse;
import com.example.livraison.entity.Client;
import com.example.livraison.entity.Livreur;
import com.example.livraison.entity.Role;
import com.example.livraison.entity.StatutLivreur;
import com.example.livraison.entity.Utilisateur;
import com.example.livraison.exception.DuplicateResourceException;
import com.example.livraison.exception.ResourceNotFoundException;
import com.example.livraison.mapper.UserMapper;
import com.example.livraison.repository.ClientRepository;
import com.example.livraison.repository.LivreurRepository;
import com.example.livraison.repository.UtilisateurRepository;
import com.example.livraison.security.JwtService;
import com.example.livraison.service.AuthService;
import com.example.livraison.util.SecurityUtils;
import java.util.Set;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final ClientRepository clientRepository;
    private final LivreurRepository livreurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UtilisateurRepository utilisateurRepository,
                           ClientRepository clientRepository, LivreurRepository livreurRepository,
                           PasswordEncoder passwordEncoder, JwtService jwtService, UserMapper userMapper,
                           AuthenticationManager authenticationManager) {
        this.utilisateurRepository = utilisateurRepository;
        this.clientRepository = clientRepository;
        this.livreurRepository = livreurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (utilisateurRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Le nom d'utilisateur existe deja : " + request.getUsername());
        }
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("L'email existe deja : " + request.getEmail());
        }
        Role role = request.getRole() == null ? Role.CLIENT : request.getRole();

        Utilisateur utilisateur = userMapper.toEntity(request);
        utilisateur.setPassword(passwordEncoder.encode(request.getPassword()));
        Utilisateur saved = utilisateurRepository.save(utilisateur);

        if (role == Role.CLIENT) {
            clientRepository.save(Client.builder()
                    .nom(request.getNom())
                    .prenom(request.getPrenom())
                    .telephone(request.getTelephone())
                    .email(request.getEmail())
                    .utilisateur(saved)
                    .build());
        } else if (role == Role.LIVREUR) {
            livreurRepository.save(Livreur.builder()
                    .nom(request.getNom())
                    .prenom(request.getPrenom())
                    .telephone(request.getTelephone())
                    .email(request.getEmail())
                    .statut(StatutLivreur.DISPONIBLE)
                    .utilisateur(saved)
                    .build());
        }

        String token = jwtService.generateToken(toUserDetails(saved));
        return buildAuthResponse(token, saved);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Utilisateur utilisateur = utilisateurRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
        String token = jwtService.generateToken(userDetails);
        return buildAuthResponse(token, utilisateur);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse currentUser() {
        String username = SecurityUtils.getCurrentUsername()
                .orElseThrow(() -> new ResourceNotFoundException("Aucun utilisateur authentifie"));
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + username));
        return userMapper.toResponse(utilisateur);
    }

    private AuthResponse buildAuthResponse(String token, Utilisateur utilisateur) {
        return new AuthResponse(token, "Bearer", utilisateur.getId(), utilisateur.getUsername(),
                Set.of(utilisateur.getRole().name()));
    }

    private UserDetails toUserDetails(Utilisateur utilisateur) {
        return User.builder()
                .username(utilisateur.getUsername())
                .password(utilisateur.getPassword())
                .authorities(new SimpleGrantedAuthority("ROLE_" + utilisateur.getRole().name()))
                .disabled(!utilisateur.isActif())
                .build();
    }
}