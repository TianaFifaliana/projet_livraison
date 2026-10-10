package com.example.livraison.repository;

import com.example.livraison.entity.Paiement;
import com.example.livraison.entity.StatutPaiement;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    Optional<Paiement> findByCommandeId(Long commandeId);

    List<Paiement> findByStatut(StatutPaiement statut);

    long countByStatut(StatutPaiement statut);
}
