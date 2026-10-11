package com.example.livraison.mapper;

import com.example.livraison.dto.request.CreateClientRequest;
import com.example.livraison.dto.response.ClientResponse;
import com.example.livraison.entity.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public ClientResponse toResponse(Client client) {
        return new ClientResponse(
                client.getId(),
                client.getNom(),
                client.getPrenom(),
                client.getTelephone(),
                client.getEmail(),
                client.getAdresse(),
                client.getUtilisateur() != null ? client.getUtilisateur().getId() : null);
    }

    public Client toEntity(CreateClientRequest request) {
        return Client.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .telephone(request.getTelephone())
                .email(request.getEmail())
                .adresse(request.getAdresse())
                .build();
    }

    public void apply(CreateClientRequest request, Client client) {
        client.setNom(request.getNom());
        client.setPrenom(request.getPrenom());
        client.setTelephone(request.getTelephone());
        client.setEmail(request.getEmail());
        client.setAdresse(request.getAdresse());
    }
}