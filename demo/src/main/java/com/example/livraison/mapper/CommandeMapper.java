package com.example.livraison.mapper;

import com.example.livraison.dto.request.CreateCommandeRequest;
import com.example.livraison.dto.request.UpdateCommandeRequest;
import com.example.livraison.dto.response.CommandeResponse;
import com.example.livraison.entity.Commande;
import com.example.livraison.entity.StatutCommande;
import org.springframework.stereotype.Component;

@Component
public class CommandeMapper {

    public CommandeResponse toResponse(Commande commande) {
        return new CommandeResponse(
                commande.getId(),
                commande.getNumero(),
                commande.getAdresseEnlevement(),
                commande.getAdresseLivraison(),
                commande.getPoids(),
                commande.getVolume(),
                commande.getPriorite() != null ? commande.getPriorite().name() : null,
                commande.getDistance(),
                commande.getPrix(),
                commande.getDatePrevue(),
                commande.getDateCreation(),
                commande.getStatut() != null ? commande.getStatut().name() : null,
                commande.getClient() != null ? commande.getClient().getId() : null,
                commande.getClient() != null
                        ? commande.getClient().getPrenom() + " " + commande.getClient().getNom()
                        : null,
                commande.getLivreur() != null ? commande.getLivreur().getId() : null,
                commande.getZone() != null ? commande.getZone().getId() : null);
    }

    public Commande toEntity(CreateCommandeRequest request) {
        return Commande.builder()
                .adresseEnlevement(request.getAdresseEnlevement())
                .adresseLivraison(request.getAdresseLivraison())
                .poids(request.getPoids())
                .volume(request.getVolume())
                .priorite(request.getPriorite())
                .distance(request.getDistance())
                .prix(request.getPrix())
                .datePrevue(request.getDatePrevue())
                .statut(request.getStatut() != null ? request.getStatut() : StatutCommande.CREEE)
                .build();
    }

    public void apply(UpdateCommandeRequest request, Commande commande) {
        commande.setAdresseEnlevement(request.getAdresseEnlevement());
        commande.setAdresseLivraison(request.getAdresseLivraison());
        commande.setPoids(request.getPoids());
        commande.setVolume(request.getVolume());
        commande.setPriorite(request.getPriorite());
        commande.setDistance(request.getDistance());
        commande.setPrix(request.getPrix());
        commande.setDatePrevue(request.getDatePrevue());
        if (request.getStatut() != null) {
            commande.setStatut(request.getStatut());
        }
    }
}