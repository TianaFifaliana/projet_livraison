package com.example.livraison.service.impl;

import com.example.livraison.dto.request.ClientRequest;
import com.example.livraison.dto.response.ClientResponse;
import com.example.livraison.entity.Client;
import com.example.livraison.entity.User;
import com.example.livraison.exception.DuplicateResourceException;
import com.example.livraison.exception.ResourceNotFoundException;
import com.example.livraison.mapper.ClientMapper;
import com.example.livraison.repository.ClientRepository;
import com.example.livraison.repository.UserRepository;
import com.example.livraison.service.ClientService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;

    public ClientServiceImpl(ClientRepository clientRepository, UserRepository userRepository,
                             ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    public ClientResponse create(ClientRequest request) {
        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Un client avec cet email existe deja : " + request.getEmail());
        }
        Client client = clientMapper.toEntity(request);
        client.setUser(resolveUser(request.getUserId()));
        return clientMapper.toResponse(clientRepository.save(client));
    }

    @Override
    public ClientResponse update(Long id, ClientRequest request) {
        Client client = findClient(id);
        if (!client.getEmail().equals(request.getEmail()) && clientRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Un client avec cet email existe deja : " + request.getEmail());
        }
        clientMapper.apply(request, client);
        client.setUser(resolveUser(request.getUserId()));
        return clientMapper.toResponse(clientRepository.save(client));
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponse getById(Long id) {
        return clientMapper.toResponse(findClient(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientResponse> getAll() {
        return clientRepository.findAll().stream().map(clientMapper::toResponse).toList();
    }

    @Override
    public void delete(Long id) {
        Client client = findClient(id);
        clientRepository.delete(client);
    }

    private Client findClient(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client", id));
    }

    private User resolveUser(Long userId) {
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }
}
