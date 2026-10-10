package com.example.livraison.service;

import com.example.livraison.dto.request.UserRequest;
import com.example.livraison.dto.response.UserResponse;
import java.util.List;

public interface UserService {

    UserResponse create(UserRequest request);

    UserResponse update(Long id, UserRequest request);

    UserResponse getById(Long id);

    UserResponse getByUsername(String username);

    List<UserResponse> getAll();

    void delete(Long id);

    void changePassword(Long id, String newPassword);
}
