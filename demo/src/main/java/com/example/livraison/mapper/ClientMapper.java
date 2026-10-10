package com.example.livraison.mapper;

import com.example.livraison.dto.request.ClientRequest;
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
                client.getDateInscription(),
                client.getUser() != null ? client.getUser().getId() : null);
    }

    public Client toEntity(ClientRequest request) {
        return Client.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .telephone(request.getTelephone())
                .email(request.getEmail())
                .build();
    }

    public void apply(ClientRequest request, Client client) {
        client.setNom(request.getNom());
        client.setPrenom(request.getPrenom());
        client.setTelephone(request.getTelephone());
        client.setEmail(request.getEmail());
    }
}
