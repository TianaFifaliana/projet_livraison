package com.example.livraison.service.impl;

import com.example.livraison.dto.response.CommandeResponse;
import com.example.livraison.entity.Commande;
import com.example.livraison.entity.Livreur;
import com.example.livraison.entity.StatutCommande;
import com.example.livraison.entity.StatutLivreur;
import com.example.livraison.entity.Vehicule;
import com.example.livraison.exception.BusinessException;
import com.example.livraison.exception.ResourceNotFoundException;
import com.example.livraison.mapper.CommandeMapper;
import com.example.livraison.repository.CommandeRepository;
import com.example.livraison.repository.LivreurRepository;
import com.example.livraison.service.AttributionService;
import com.example.livraison.util.StatutTransitionValidator;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AttributionServiceImpl implements AttributionService {

    private static final double COEFFICIENT_DISTANCE = 1.0;
    private static final double COEFFICIENT_CHARGE = 5.0;

    private static final Set<StatutCommande> STATUTS_ACTIFS = Set.of(
            StatutCommande.ASSIGNEE,
            StatutCommande.PRISE_EN_CHARGE,
            StatutCommande.EN_TRANSIT);

    private final CommandeRepository commandeRepository;
    private final LivreurRepository livreurRepository;
    private final StatutTransitionValidator statutTransitionValidator;
    private final CommandeMapper commandeMapper;

    public AttributionServiceImpl(CommandeRepository commandeRepository, LivreurRepository livreurRepository,
                                  StatutTransitionValidator statutTransitionValidator,
                                  CommandeMapper commandeMapper) {
        this.commandeRepository = commandeRepository;
        this.livreurRepository = livreurRepository;
        this.statutTransitionValidator = statutTransitionValidator;
        this.commandeMapper = commandeMapper;
    }

    @Override
    public CommandeResponse attribuerLivreur(Long commandeId) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande", commandeId));

        statutTransitionValidator.validerTransition(commande.getStatut(), StatutCommande.ASSIGNEE);

        Long zoneId = commande.getZone() != null ? commande.getZone().getId() : null;
        if (zoneId == null) {
            throw new BusinessException("Impossible d'attribuer un livreur : la commande n'a pas de zone.");
        }

        List<Livreur> candidats = livreurRepository.findByStatutAndZoneId(StatutLivreur.DISPONIBLE, zoneId).stream()
                .filter(livreur -> aCapaciteSuffisante(livreur, commande))
                .toList();

        if (candidats.isEmpty()) {
            throw new BusinessException(
                    "Aucun livreur compatible disponible pour la zone " + zoneId + ".");
        }

        Livreur meilleurLivreur = null;
        double meilleurScore = Double.MAX_VALUE;
        for (Livreur livreur : candidats) {
            long livraisonsAffectees =
                    commandeRepository.countByLivreurIdAndStatutIn(livreur.getId(), STATUTS_ACTIFS);
            double score = calculerScore(commande, livraisonsAffectees);
            if (score < meilleurScore) {
                meilleurScore = score;
                meilleurLivreur = livreur;
            }
        }

        if (meilleurLivreur.getStatut() != StatutLivreur.EN_LIVRAISON) {
            meilleurLivreur.setStatut(StatutLivreur.EN_LIVRAISON);
        }
        commande.setLivreur(meilleurLivreur);
        commande.setStatut(StatutCommande.ASSIGNEE);
        livreurRepository.save(meilleurLivreur);
        commandeRepository.save(commande);

        return commandeMapper.toResponse(commande);
    }

    private double calculerScore(Commande commande, long livraisonsAffectees) {
        double distance = commande.getDistance() != null ? commande.getDistance() : 0.0;
        double coefficientPriorite = commande.getPriorite() != null
                ? commande.getPriorite().getCoefficient().doubleValue()
                : 1.0;
        return (COEFFICIENT_DISTANCE * distance)
                + (COEFFICIENT_CHARGE * livraisonsAffectees * coefficientPriorite);
    }

    private boolean aCapaciteSuffisante(Livreur livreur, Commande commande) {
        Vehicule vehicule = livreur.getVehicule();
        if (vehicule == null) {
            return false;
        }
        double poids = commande.getPoids() != null ? commande.getPoids() : 0.0;
        double volume = commande.getVolume() != null ? commande.getVolume() : 0.0;
        double capacitePoids = vehicule.getCapacitePoids() != null ? vehicule.getCapacitePoids() : 0.0;
        double capaciteVolume = vehicule.getCapaciteVolume() != null ? vehicule.getCapaciteVolume() : 0.0;
        return capacitePoids >= poids && capaciteVolume >= volume;
    }
}
