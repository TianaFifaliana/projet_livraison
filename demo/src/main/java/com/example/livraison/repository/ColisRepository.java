package com.example.livraison.repository;

import com.example.livraison.entity.Colis;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColisRepository extends JpaRepository<Colis, Long> {

    List<Colis> findByCommandeId(Long commandeId);

    long countByCommandeId(Long commandeId);
}
