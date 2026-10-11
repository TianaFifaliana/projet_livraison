package com.example.livraison.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.livraison.entity.Client;
import com.example.livraison.entity.Commande;
import com.example.livraison.entity.Livreur;
import com.example.livraison.entity.Priorite;
import com.example.livraison.entity.StatutCommande;
import com.example.livraison.entity.StatutLivreur;
import com.example.livraison.entity.Vehicule;
import com.example.livraison.entity.Zone;
import com.example.livraison.exception.BusinessException;
import com.example.livraison.exception.InvalidOperationException;
import com.example.livraison.exception.ResourceNotFoundException;
import com.example.livraison.mapper.CommandeMapper;
import com.example.livraison.repository.CommandeRepository;
import com.example.livraison.repository.LivreurRepository;
import com.example.livraison.util.StatutTransitionValidator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AttributionServiceImplTest {

    @Mock
    private CommandeRepository commandeRepository;

    @Mock
    private LivreurRepository livreurRepository;

    private final StatutTransitionValidator statutTransitionValidator = new StatutTransitionValidator();
    private final CommandeMapper commandeMapper = new CommandeMapper();

    private AttributionServiceImpl attributionService;

    @BeforeEach
    void setUp() {
        attributionService = new AttributionServiceImpl(commandeRepository, livreurRepository,
                statutTransitionValidator, commandeMapper);
    }

    @Test
    @DisplayName("commande inconnue -> ResourceNotFoundException")
    void commandeIntrouvable() {
        when(commandeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> attributionService.attribuerLivreur(99L));
    }

    @Test
    @DisplayName("commande sans zone -> BusinessException")
    void commandeSansZone() {
        Commande commande = Commande.builder()
                .id(1L)
                .statut(StatutCommande.CREEE)
                .build();
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));

        assertThrows(BusinessException.class, () -> attributionService.attribuerLivreur(1L));
    }

    @Test
    @DisplayName("aucun livreur disponible dans la zone -> BusinessException")
    void aucunCandidat() {
        Commande commande = Commande.builder()
                .id(1L)
                .statut(StatutCommande.CREEE)
                .zone(Zone.builder().id(1L).build())
                .build();
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));
        when(livreurRepository.findByStatutAndZoneId(StatutLivreur.DISPONIBLE, 1L)).thenReturn(List.of());

        assertThrows(BusinessException.class, () -> attributionService.attribuerLivreur(1L));
    }

    @Test
    @DisplayName("aucun candidat n'a la capacite suffisante -> BusinessException")
    void aucunCandidatAvecVehicule() {
        Commande commande = Commande.builder()
                .id(1L)
                .statut(StatutCommande.CREEE)
                .zone(Zone.builder().id(1L).build())
                .poids(100.0)
                .volume(50.0)
                .build();
        Livreur sansVehicule = Livreur.builder()
                .id(1L)
                .statut(StatutLivreur.DISPONIBLE)
                .vehicule(null)
                .build();
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));
        when(livreurRepository.findByStatutAndZoneId(StatutLivreur.DISPONIBLE, 1L))
                .thenReturn(List.of(sansVehicule));

        assertThrows(BusinessException.class, () -> attributionService.attribuerLivreur(1L));
    }

    @Test
    @DisplayName("le livreur avec le meilleur score est retenu")
    void choisitMeilleurLivreur() {
        Vehicule vehicule = Vehicule.builder().capacitePoids(500.0).capaciteVolume(10.0).build();
        Livreur charge = Livreur.builder()
                .id(11L)
                .statut(StatutLivreur.DISPONIBLE)
                .vehicule(vehicule)
                .build();
        Livreur libre = Livreur.builder()
                .id(12L)
                .statut(StatutLivreur.DISPONIBLE)
                .vehicule(vehicule)
                .build();

        Client client = Client.builder().id(1L).prenom("Awa").nom("Diallo").build();
        Zone zone = Zone.builder().id(1L).build();
        Commande commande = Commande.builder()
                .id(1L)
                .numero("CMD-X")
                .adresseEnlevement("Entrepot")
                .adresseLivraison("Chez le client")
                .poids(10.0)
                .volume(2.0)
                .priorite(Priorite.NORMALE)
                .distance(10.0)
                .prix(new BigDecimal("20.00"))
                .datePrevue(LocalDateTime.now().plusDays(1))
                .dateCreation(LocalDateTime.now())
                .statut(StatutCommande.CREEE)
                .client(client)
                .zone(zone)
                .build();

        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));
        when(livreurRepository.findByStatutAndZoneId(StatutLivreur.DISPONIBLE, 1L))
                .thenReturn(List.of(charge, libre));
        when(commandeRepository.countByLivreurIdAndStatutIn(eq(11L), any())).thenReturn(2L);
        when(commandeRepository.countByLivreurIdAndStatutIn(eq(12L), any())).thenReturn(0L);

        var result = attributionService.attribuerLivreur(1L);

        assertEquals(12L, result.livreurId());
        assertEquals("Awa Diallo", result.clientNom());
        assertEquals(libre, commande.getLivreur());
        assertEquals(StatutCommande.ASSIGNEE, commande.getStatut());
        assertEquals(StatutLivreur.EN_LIVRAISON, libre.getStatut());
        verify(commandeRepository).save(commande);
        verify(livreurRepository).save(libre);
    }

    @Test
    @DisplayName("l'attribution d'une commande deja finalisee est refusee")
    void commandeFinalisee() {
        Commande commande = Commande.builder()
                .id(1L)
                .statut(StatutCommande.LIVREE)
                .zone(Zone.builder().id(1L).build())
                .build();
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));

        assertThrows(InvalidOperationException.class, () -> attributionService.attribuerLivreur(1L));
    }
}