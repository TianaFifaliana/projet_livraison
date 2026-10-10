package com.example.livraison.service.impl;

import com.example.livraison.dto.request.UserRequest;
import com.example.livraison.dto.response.UserResponse;
import com.example.livraison.entity.Role;
import com.example.livraison.entity.RoleName;
import com.example.livraison.entity.User;
import com.example.livraison.exception.DuplicateResourceException;
import com.example.livraison.exception.ResourceNotFoundException;
import com.example.livraison.mapper.UserMapper;
import com.example.livraison.repository.RoleRepository;
import com.example.livraison.repository.UserRepository;
import com.example.livraison.service.UserService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse create(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Le nom d'utilisateur existe deja : " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("L'email existe deja : " + request.getEmail());
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new com.example.livraison.exception.BusinessException("Le mot de passe est obligatoire");
        }
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(resolveRoles(request.getRoles()));
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse update(Long id, UserRequest request) {
        User user = findUser(id);
        if (!user.getUsername().equals(request.getUsername()) && userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Le nom d'utilisateur existe deja : " + request.getUsername());
        }
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("L'email existe deja : " + request.getEmail());
        }
        userMapper.apply(request, user);
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user.setRoles(resolveRoles(request.getRoles()));
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        return userMapper.toResponse(findUser(id));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + username));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAll() {
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }

    @Override
    public void delete(Long id) {
        User user = findUser(id);
        userRepository.delete(user);
    }

    @Override
    public void changePassword(Long id, String newPassword) {
        User user = findUser(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    private Set<Role> resolveRoles(Set<RoleName> roles) {
        if (roles == null || roles.isEmpty()) {
            throw new com.example.livraison.exception.BusinessException("Au moins un role est requis");
        }
        return roles.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new ResourceNotFoundException("Role introuvable : " + roleName)))
                .collect(Collectors.toSet());
    }
}
