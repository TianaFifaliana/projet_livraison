package com.example.livraison.repository;

import com.example.livraison.entity.Commande;
import com.example.livraison.entity.StatutCommande;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeRepository extends JpaRepository<Commande, Long> {

    Optional<Commande> findByReference(String reference);

    List<Commande> findByClientId(Long clientId);

    List<Commande> findByStatut(StatutCommande statut);

    boolean existsByReference(String reference);

    long countByStatut(StatutCommande statut);
}
