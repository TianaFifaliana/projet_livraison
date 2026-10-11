package com.example.livraison.service.impl;

import com.example.livraison.dto.request.PeriodeStatistique;
import com.example.livraison.dto.request.StatistiquesRequest;
import com.example.livraison.dto.response.StatistiquesResponse;
import com.example.livraison.entity.Commande;
import com.example.livraison.entity.Livraison;
import com.example.livraison.entity.StatutLivraison;
import com.example.livraison.entity.StatutPaiement;
import com.example.livraison.repository.LivraisonRepository;
import com.example.livraison.repository.PaiementRepository;
import com.example.livraison.service.StatistiqueService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StatistiqueServiceImpl implements StatistiqueService {

    private final LivraisonRepository livraisonRepository;
    private final PaiementRepository paiementRepository;

    public StatistiqueServiceImpl(LivraisonRepository livraisonRepository, PaiementRepository paiementRepository) {
        this.livraisonRepository = livraisonRepository;
        this.paiementRepository = paiementRepository;
    }

    @Override
    public StatistiquesResponse calculerStatistiques(StatistiquesRequest request) {
        Long livreurId = request != null ? request.getLivreurId() : null;
        Long zoneId = request != null ? request.getZoneId() : null;
        LocalDateTime debut = resoudreDateDebut(request);
        LocalDateTime fin = resoudreDateFin(request, debut);

        List<Livraison> livraisons =
                livraisonRepository.rechercherPourStatistiques(livreurId, zoneId, debut, fin);

        long nombreLivraisons = livraisons.size();
        long livrees = livraisons.stream()
                .filter(l -> l.getStatut() == StatutLivraison.LIVREE)
                .count();
        long echouees = livraisons.stream()
                .filter(l -> l.getStatut() == StatutLivraison.ECHOUEE)
                .count();

        double tauxReussite = pourcentage(livrees, nombreLivraisons);
        double tauxEchec = pourcentage(echouees, nombreLivraisons);
        double delaiMoyenHeures = calculerDelaiMoyenHeures(livraisons);
        double distanceTotale = livraisons.stream()
                .map(Livraison::getCommande)
                .filter(commande -> commande != null && commande.getDistance() != null)
                .mapToDouble(Commande::getDistance)
                .sum();

        BigDecimal chiffreAffaires = paiementRepository.sommeMontantParStatut(
                StatutPaiement.PAYE, livreurId, zoneId, debut, fin);
        if (chiffreAffaires == null) {
            chiffreAffaires = BigDecimal.ZERO;
        }
        BigDecimal revenuMoyen = nombreLivraisons == 0
                ? BigDecimal.ZERO
                : chiffreAffaires.divide(BigDecimal.valueOf(nombreLivraisons), 2, RoundingMode.HALF_UP);

        return new StatistiquesResponse(
                nombreLivraisons,
                tauxReussite,
                tauxEchec,
                arrondir(delaiMoyenHeures),
                chiffreAffaires.setScale(2, RoundingMode.HALF_UP),
                compterParLivreur(livraisons),
                revenuMoyen,
                arrondir(distanceTotale));
    }

    private Map<Long, Long> compterParLivreur(List<Livraison> livraisons) {
        Map<Long, Long> compteurs = new HashMap<>();
        for (Livraison livraison : livraisons) {
            if (livraison.getLivreur() != null) {
                compteurs.merge(livraison.getLivreur().getId(), 1L, Long::sum);
            }
        }
        return compteurs;
    }

    private double calculerDelaiMoyenHeures(List<Livraison> livraisons) {
        long totalMinutes = 0;
        long nombre = 0;
        for (Livraison livraison : livraisons) {
            if (livraison.getStatut() != StatutLivraison.LIVREE || livraison.getDateLivraisonReelle() == null) {
                continue;
            }
            Commande commande = livraison.getCommande();
            if (commande == null || commande.getDateCreation() == null) {
                continue;
            }
            totalMinutes += Duration.between(commande.getDateCreation(), livraison.getDateLivraisonReelle()).toMinutes();
            nombre++;
        }
        return nombre == 0 ? 0.0 : (totalMinutes / 60.0) / nombre;
    }

    private LocalDateTime resoudreDateDebut(StatistiquesRequest request) {
        if (request == null) {
            return null;
        }
        if (request.getDateDebut() != null) {
            return request.getDateDebut();
        }
        PeriodeStatistique periode = request.getPeriode();
        if (periode == null || periode == PeriodeStatistique.TOUT) {
            return null;
        }
        LocalDate aujourdHui = LocalDate.now();
        return switch (periode) {
            case JOUR -> aujourdHui.atStartOfDay();
            case SEMAINE -> aujourdHui.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)).atStartOfDay();
            case MOIS -> aujourdHui.withDayOfMonth(1).atStartOfDay();
            case TOUT -> null;
        };
    }

    private LocalDateTime resoudreDateFin(StatistiquesRequest request, LocalDateTime debut) {
        if (request != null && request.getDateFin() != null) {
            return request.getDateFin();
        }
        if (debut == null) {
            return null;
        }
        return LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    }

    private double pourcentage(long valeur, long total) {
        return total == 0 ? 0.0 : arrondir((valeur * 100.0) / total);
    }

    private double arrondir(double valeur) {
        return BigDecimal.valueOf(valeur).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
