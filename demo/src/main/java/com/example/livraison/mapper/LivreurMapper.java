package com.example.livraison.mapper;

import com.example.livraison.dto.request.LivreurRequest;
import com.example.livraison.dto.response.LivreurResponse;
import com.example.livraison.entity.Livreur;
import org.springframework.stereotype.Component;

@Component
public class LivreurMapper {

    public LivreurResponse toResponse(Livreur livreur) {
        return new LivreurResponse(
                livreur.getId(),
                livreur.getNom(),
                livreur.getPrenom(),
                livreur.getTelephone(),
                livreur.getEmail(),
                livreur.getVehicule(),
                livreur.isDisponible(),
                livreur.getUser() != null ? livreur.getUser().getId() : null);
    }

    public Livreur toEntity(LivreurRequest request) {
        return Livreur.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .telephone(request.getTelephone())
                .email(request.getEmail())
                .vehicule(request.getVehicule())
                .disponible(request.isDisponible())
                .build();
    }

    public void apply(LivreurRequest request, Livreur livreur) {
        livreur.setNom(request.getNom());
        livreur.setPrenom(request.getPrenom());
        livreur.setTelephone(request.getTelephone());
        livreur.setEmail(request.getEmail());
        livreur.setVehicule(request.getVehicule());
        livreur.setDisponible(request.isDisponible());
    }
}
