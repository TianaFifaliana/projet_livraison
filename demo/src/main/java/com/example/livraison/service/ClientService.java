package com.example.livraison.service;

import com.example.livraison.dto.request.ClientRequest;
import com.example.livraison.dto.response.ClientResponse;
import java.util.List;

public interface ClientService {

    ClientResponse create(ClientRequest request);

    ClientResponse update(Long id, ClientRequest request);

    ClientResponse getById(Long id);

    List<ClientResponse> getAll();

    void delete(Long id);
}
