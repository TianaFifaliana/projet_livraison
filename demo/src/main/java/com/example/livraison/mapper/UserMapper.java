package com.example.livraison.mapper;

import com.example.livraison.dto.request.RegisterRequest;
import com.example.livraison.dto.request.UserRequest;
import com.example.livraison.dto.response.UserResponse;
import com.example.livraison.entity.Role;
import com.example.livraison.entity.User;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.isEnabled(),
                user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet()));
    }

    public User toEntity(UserRequest request) {
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .enabled(request.isEnabled())
                .build();
    }

    public User toEntity(RegisterRequest request) {
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .enabled(true)
                .build();
    }

    public void apply(UserRequest request, User user) {
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setEnabled(request.isEnabled());
    }

    public void assignRoles(Set<Role> roles, User user) {
        user.setRoles(roles);
    }
}
