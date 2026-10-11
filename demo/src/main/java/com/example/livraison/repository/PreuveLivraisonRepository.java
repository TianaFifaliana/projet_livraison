package com.example.livraison.repository;

import com.example.livraison.entity.PreuveLivraison;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreuveLivraisonRepository extends JpaRepository<PreuveLivraison, Long> {

    Optional<PreuveLivraison> findByCommandeId(Long commandeId);

    boolean existsByCommandeId(Long commandeId);
}
