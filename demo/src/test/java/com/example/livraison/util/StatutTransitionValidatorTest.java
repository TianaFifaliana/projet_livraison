package com.example.livraison.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.livraison.entity.StatutCommande;
import com.example.livraison.exception.InvalidOperationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StatutTransitionValidatorTest {

    private final StatutTransitionValidator validator = new StatutTransitionValidator();

    @Test
    @DisplayName("une transition valide est autorisee")
    void transitionValide() {
        assertTrue(validator.estAutorisee(StatutCommande.CREEE, StatutCommande.ASSIGNEE));
        assertTrue(validator.estAutorisee(StatutCommande.CREEE, StatutCommande.ANNULEE));
        assertTrue(validator.estAutorisee(StatutCommande.ASSIGNEE, StatutCommande.PRISE_EN_CHARGE));
        assertTrue(validator.estAutorisee(StatutCommande.ASSIGNEE, StatutCommande.ANNULEE));
        assertTrue(validator.estAutorisee(StatutCommande.PRISE_EN_CHARGE, StatutCommande.EN_TRANSIT));
        assertTrue(validator.estAutorisee(StatutCommande.EN_TRANSIT, StatutCommande.LIVREE));
        assertTrue(validator.estAutorisee(StatutCommande.EN_TRANSIT, StatutCommande.ECHEC_LIVRAISON));
        assertTrue(validator.estAutorisee(StatutCommande.ECHEC_LIVRAISON, StatutCommande.RETOURNEE));
        assertTrue(validator.estAutorisee(StatutCommande.ECHEC_LIVRAISON, StatutCommande.ASSIGNEE));
    }

    @Test
    @DisplayName("une transition vers un etat final est interdite")
    void transitionDepuisEtatFinal() {
        assertFalse(validator.estAutorisee(StatutCommande.LIVREE, StatutCommande.EN_TRANSIT));
        assertFalse(validator.estAutorisee(StatutCommande.ANNULEE, StatutCommande.CREEE));
        assertFalse(validator.estAutorisee(StatutCommande.RETOURNEE, StatutCommande.ASSIGNEE));
    }

    @Test
    @DisplayName("un saut d'etape est interdit")
    void transitionSautEtape() {
        assertFalse(validator.estAutorisee(StatutCommande.CREEE, StatutCommande.EN_TRANSIT));
        assertFalse(validator.estAutorisee(StatutCommande.CREEE, StatutCommande.LIVREE));
        assertFalse(validator.estAutorisee(StatutCommande.ASSIGNEE, StatutCommande.EN_TRANSIT));
        assertFalse(validator.estAutorisee(StatutCommande.PRISE_EN_CHARGE, StatutCommande.LIVREE));
    }

    @Test
    @DisplayName("un statut nul est toujours refuse")
    void statutsNuls() {
        assertFalse(validator.estAutorisee(null, StatutCommande.ASSIGNEE));
        assertFalse(validator.estAutorisee(StatutCommande.CREEE, null));
        assertFalse(validator.estAutorisee(null, null));
        assertFalse(validator.estEtatFinal(null));
    }

    @Test
    @DisplayName("les etats finaux sont identifies")
    void etatsFinaux() {
        assertTrue(validator.estEtatFinal(StatutCommande.LIVREE));
        assertTrue(validator.estEtatFinal(StatutCommande.ANNULEE));
        assertTrue(validator.estEtatFinal(StatutCommande.RETOURNEE));
        assertFalse(validator.estEtatFinal(StatutCommande.CREEE));
        assertFalse(validator.estEtatFinal(StatutCommande.EN_TRANSIT));
    }

    @Test
    @DisplayName("validerTransition accepte une transition autorisee")
    void validerTransitionOK() {
        validator.validerTransition(StatutCommande.CREEE, StatutCommande.ASSIGNEE);
    }

    @Test
    @DisplayName("validerTransition rejette un saut d'etape")
    void validerTransitionInvalide() {
        assertThrows(InvalidOperationException.class,
                () -> validator.validerTransition(StatutCommande.CREEE, StatutCommande.LIVREE));
    }

    @Test
    @DisplayName("validerTransition rejette une transition depuis un etat final")
    void validerTransitionDepuisEtatFinal() {
        InvalidOperationException ex = assertThrows(InvalidOperationException.class,
                () -> validator.validerTransition(StatutCommande.LIVREE, StatutCommande.ASSIGNEE));
        assertTrue(ex.getMessage().contains("etat final"));
    }
}