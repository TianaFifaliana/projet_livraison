package com.example.livraison.mapper;

import com.example.livraison.dto.request.CreateLivreurRequest;
import com.example.livraison.dto.response.LivreurResponse;
import com.example.livraison.entity.Livreur;
import com.example.livraison.entity.StatutLivreur;
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
                livreur.getStatut() != null ? livreur.getStatut().name() : null,
                livreur.getZone() != null ? livreur.getZone().getId() : null,
                livreur.getVehicule() != null ? livreur.getVehicule().getId() : null,
                livreur.getVehicule() != null ? livreur.getVehicule().getImmatriculation() : null,
                livreur.getUtilisateur() != null ? livreur.getUtilisateur().getId() : null);
    }

    public Livreur toEntity(CreateLivreurRequest request) {
        return Livreur.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .telephone(request.getTelephone())
                .email(request.getEmail())
                .statut(request.getStatut() != null ? request.getStatut() : StatutLivreur.DISPONIBLE)
                .build();
    }

    public void apply(CreateLivreurRequest request, Livreur livreur) {
        livreur.setNom(request.getNom());
        livreur.setPrenom(request.getPrenom());
        livreur.setTelephone(request.getTelephone());
        livreur.setEmail(request.getEmail());
        if (request.getStatut() != null) {
            livreur.setStatut(request.getStatut());
        }
    }
}