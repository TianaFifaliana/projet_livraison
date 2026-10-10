package com.example.livraison.mapper;

import com.example.livraison.dto.request.CommandeRequest;
import com.example.livraison.dto.response.ColisResponse;
import com.example.livraison.dto.response.CommandeResponse;
import com.example.livraison.entity.Commande;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CommandeMapper {

    private final AdresseMapper adresseMapper;
    private final ColisMapper colisMapper;

    public CommandeMapper(AdresseMapper adresseMapper, ColisMapper colisMapper) {
        this.adresseMapper = adresseMapper;
        this.colisMapper = colisMapper;
    }

    public CommandeResponse toResponse(Commande commande) {
        List<ColisResponse> colis = commande.getColis().stream()
                .map(colisMapper::toResponse)
                .toList();
        return new CommandeResponse(
                commande.getId(),
                commande.getReference(),
                commande.getDateCommande(),
                commande.getStatut().name(),
                commande.getMontantTotal(),
                commande.getClient() != null ? commande.getClient().getId() : null,
                commande.getClient() != null
                        ? commande.getClient().getPrenom() + " " + commande.getClient().getNom()
                        : null,
                commande.getAdresseLivraison() != null ? adresseMapper.toResponse(commande.getAdresseLivraison()) : null,
                colis,
                commande.getLivraison() != null ? commande.getLivraison().getId() : null,
                commande.getPaiement() != null ? commande.getPaiement().getId() : null);
    }

    public Commande toEntity(CommandeRequest request) {
        return Commande.builder()
                .statut(request.getStatut())
                .montantTotal(request.getMontantTotal())
                .colis(new ArrayList<>())
                .build();
    }

    public void apply(CommandeRequest request, Commande commande) {
        commande.setStatut(request.getStatut());
        commande.setMontantTotal(request.getMontantTotal());
    }
}
