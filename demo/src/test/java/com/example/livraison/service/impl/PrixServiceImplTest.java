package com.example.livraison.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.livraison.dto.request.CalculPrixRequest;
import com.example.livraison.dto.response.PrixResponse;
import com.example.livraison.entity.Priorite;
import com.example.livraison.entity.Tarif;
import com.example.livraison.repository.TarifRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PrixServiceImplTest {

    @Mock
    private TarifRepository tarifRepository;

    @InjectMocks
    private PrixServiceImpl prixService;

    @Test
    @DisplayName("le prix est calcule avec le tarif de la zone")
    void calculeAvecTarifDeZone() {
        Tarif tarif = Tarif.builder()
                .id(7L)
                .libelle("Tarif Centre")
                .prixBase(new BigDecimal("10.00"))
                .prixParKm(new BigDecimal("1.00"))
                .prixParKg(new BigDecimal("0.50"))
                .prixParM3(new BigDecimal("2.00"))
                .coefficientZone(new BigDecimal("1.10"))
                .parDefaut(false)
                .build();
        when(tarifRepository.findFirstByZoneIdOrderByIdAsc(2L)).thenReturn(Optional.of(tarif));

        PrixResponse response = prixService.calculer(CalculPrixRequest.builder()
                .distance(10.0)
                .poids(5.0)
                .volume(2.0)
                .priorite(Priorite.NORMALE)
                .zoneId(2L)
                .build());

        assertEquals(new BigDecimal("29.15"), response.montant());
        assertEquals(new BigDecimal("10.00"), response.prixBase());
        assertEquals(new BigDecimal("1.10"), response.coefficientZone());
        assertEquals(new BigDecimal("1.00"), response.coefficientPriorite());
        assertEquals(7L, response.tarifId());
        assertEquals("Tarif Centre", response.tarifLibelle());
        assertFalse(response.tarifParDefaut());
    }

    @Test
    @DisplayName("sans tarif, les valeurs par defaut sont appliquees")
    void utiliseDefaultsSansTarif() {
        when(tarifRepository.findFirstByParDefautTrueOrderByIdAsc()).thenReturn(Optional.empty());

        PrixResponse response = prixService.calculer(CalculPrixRequest.builder()
                .distance(10.0)
                .poids(5.0)
                .volume(2.0)
                .priorite(Priorite.NORMALE)
                .build());

        assertEquals(new BigDecimal("13.00"), response.montant());
        assertEquals(new BigDecimal("5.00"), response.prixBase());
        assertEquals(new BigDecimal("0.50"), response.prixParKm());
        assertEquals(new BigDecimal("0.20"), response.prixParKg());
        assertEquals(new BigDecimal("1.00"), response.prixParM3());
        assertEquals(null, response.tarifId());
        assertEquals("Tarif par defaut", response.tarifLibelle());
        assertTrue(response.tarifParDefaut());
        verify(tarifRepository, never()).findFirstByZoneIdOrderByIdAsc(2L);
    }

    @Test
    @DisplayName("sans tarif de zone, le service retombe sur le tarif par defaut")
    void retombeSurTarifParDefaut() {
        when(tarifRepository.findFirstByZoneIdOrderByIdAsc(3L)).thenReturn(Optional.empty());
        Tarif defaut = Tarif.builder()
                .id(1L)
                .libelle("Standard")
                .prixBase(new BigDecimal("5.00"))
                .prixParKm(new BigDecimal("0.50"))
                .prixParKg(new BigDecimal("0.20"))
                .prixParM3(new BigDecimal("1.00"))
                .parDefaut(true)
                .build();
        when(tarifRepository.findFirstByParDefautTrueOrderByIdAsc()).thenReturn(Optional.of(defaut));

        PrixResponse response = prixService.calculer(CalculPrixRequest.builder()
                .distance(0.0)
                .poids(0.0)
                .volume(0.0)
                .priorite(Priorite.BASSE)
                .zoneId(3L)
                .build());

        assertEquals(new BigDecimal("4.50"), response.montant());
        assertEquals(1L, response.tarifId());
        assertTrue(response.tarifParDefaut());
        assertEquals(new BigDecimal("0.90"), response.coefficientPriorite());
    }

    @Test
    @DisplayName("des entrees nulles sont traitees comme zero")
    void valeursNulles() {
        when(tarifRepository.findFirstByParDefautTrueOrderByIdAsc()).thenReturn(Optional.empty());

        PrixResponse response = prixService.calculer(CalculPrixRequest.builder()
                .priorite(Priorite.NORMALE)
                .build());

        assertEquals(new BigDecimal("5.00"), response.montant());
    }

    @Test
    @DisplayName("calculerMontant delegue au calcul et retourne le montant uniquement")
    void calculerMontant() {
        when(tarifRepository.findFirstByParDefautTrueOrderByIdAsc()).thenReturn(Optional.empty());

        BigDecimal montant = prixService.calculerMontant(10.0, 5.0, 2.0, Priorite.NORMALE, null);

        assertEquals(new BigDecimal("13.00"), montant);
    }
}