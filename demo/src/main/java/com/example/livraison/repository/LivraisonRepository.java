package com.example.livraison.repository;

import com.example.livraison.entity.Livraison;
import com.example.livraison.entity.StatutLivraison;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivraisonRepository extends JpaRepository<Livraison, Long> {

    Optional<Livraison> findByCommandeId(Long commandeId);

    List<Livraison> findByLivreurId(Long livreurId);

    List<Livraison> findByStatut(StatutLivraison statut);

    long countByStatut(StatutLivraison statut);
}
