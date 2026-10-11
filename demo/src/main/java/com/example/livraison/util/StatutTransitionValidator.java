package com.example.livraison.util;

import com.example.livraison.entity.StatutCommande;
import com.example.livraison.exception.InvalidOperationException;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class StatutTransitionValidator {

    private static final Map<StatutCommande, Set<StatutCommande>> TRANSITIONS_AUTORISEES = Map.of(
            StatutCommande.CREEE, Set.of(StatutCommande.ASSIGNEE, StatutCommande.ANNULEE),
            StatutCommande.ASSIGNEE, Set.of(StatutCommande.PRISE_EN_CHARGE, StatutCommande.ANNULEE),
            StatutCommande.PRISE_EN_CHARGE, Set.of(StatutCommande.EN_TRANSIT),
            StatutCommande.EN_TRANSIT, Set.of(StatutCommande.LIVREE, StatutCommande.ECHEC_LIVRAISON),
            StatutCommande.ECHEC_LIVRAISON, Set.of(StatutCommande.RETOURNEE, StatutCommande.ASSIGNEE),
            StatutCommande.LIVREE, Set.of(),
            StatutCommande.ANNULEE, Set.of(),
            StatutCommande.RETOURNEE, Set.of());

    private static final Set<StatutCommande> ETATS_FINAUX =
            Set.of(StatutCommande.LIVREE, StatutCommande.ANNULEE, StatutCommande.RETOURNEE);

    public boolean estAutorisee(StatutCommande actuel, StatutCommande cible) {
        if (actuel == null || cible == null) {
            return false;
        }
        return TRANSITIONS_AUTORISEES.getOrDefault(actuel, Set.of()).contains(cible);
    }

    public boolean estEtatFinal(StatutCommande statut) {
        return statut != null && ETATS_FINAUX.contains(statut);
    }

    public void validerTransition(StatutCommande actuel, StatutCommande cible) {
        if (estAutorisee(actuel, cible)) {
            return;
        }
        if (estEtatFinal(actuel)) {
            throw new InvalidOperationException(
                    "Le statut " + actuel + " est un etat final : transition vers " + cible + " interdite.");
        }
        throw new InvalidOperationException(
                "Transition de statut invalide : " + actuel + " vers " + cible + ".");
    }
}
