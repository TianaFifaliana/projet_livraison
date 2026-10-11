package com.example.livraison.service;

import com.example.livraison.dto.request.CreateClientRequest;
import com.example.livraison.dto.response.ClientResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientService {

    ClientResponse create(CreateClientRequest request);

    ClientResponse update(Long id, CreateClientRequest request);

    ClientResponse getById(Long id);

    List<ClientResponse> getAll();

    Page<ClientResponse> rechercher(String recherche, Pageable pageable);

    void delete(Long id);
}
