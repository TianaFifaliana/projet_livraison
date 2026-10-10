package com.example.livraison.service.impl;

import com.example.livraison.dto.request.LoginRequest;
import com.example.livraison.dto.request.RegisterRequest;
import com.example.livraison.dto.response.AuthResponse;
import com.example.livraison.dto.response.UserResponse;
import com.example.livraison.entity.Client;
import com.example.livraison.entity.Livreur;
import com.example.livraison.entity.Role;
import com.example.livraison.entity.RoleName;
import com.example.livraison.entity.User;
import com.example.livraison.exception.DuplicateResourceException;
import com.example.livraison.exception.ResourceNotFoundException;
import com.example.livraison.mapper.UserMapper;
import com.example.livraison.repository.ClientRepository;
import com.example.livraison.repository.LivreurRepository;
import com.example.livraison.repository.RoleRepository;
import com.example.livraison.repository.UserRepository;
import com.example.livraison.security.JwtService;
import com.example.livraison.service.AuthService;
import com.example.livraison.util.SecurityUtils;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ClientRepository clientRepository;
    private final LivreurRepository livreurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           ClientRepository clientRepository, LivreurRepository livreurRepository,
                           PasswordEncoder passwordEncoder, JwtService jwtService, UserMapper userMapper,
                           AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.clientRepository = clientRepository;
        this.livreurRepository = livreurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Le nom d'utilisateur existe deja : " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("L'email existe deja : " + request.getEmail());
        }
        RoleName roleName = request.getRole() == null ? RoleName.CLIENT : request.getRole();
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role introuvable : " + roleName));

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(role));
        User saved = userRepository.save(user);

        if (roleName == RoleName.CLIENT) {
            clientRepository.save(Client.builder()
                    .nom(request.getNom())
                    .prenom(request.getPrenom())
                    .telephone(request.getTelephone())
                    .email(request.getEmail())
                    .user(saved)
                    .build());
        } else if (roleName == RoleName.LIVREUR) {
            livreurRepository.save(Livreur.builder()
                    .nom(request.getNom())
                    .prenom(request.getPrenom())
                    .telephone(request.getTelephone())
                    .email(request.getEmail())
                    .disponible(true)
                    .user(saved)
                    .build());
        }

        String token = jwtService.generateToken(saved);
        return buildAuthResponse(token, saved);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
        String token = jwtService.generateToken(userDetails);
        return buildAuthResponse(token, user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse currentUser() {
        String username = SecurityUtils.getCurrentUsername()
                .orElseThrow(() -> new ResourceNotFoundException("Aucun utilisateur authentifie"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + username));
        return userMapper.toResponse(user);
    }

    private AuthResponse buildAuthResponse(String token, User user) {
        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
        return new AuthResponse(token, "Bearer", user.getId(), user.getUsername(), roles);
    }
}
