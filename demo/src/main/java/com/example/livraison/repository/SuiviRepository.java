package com.example.livraison.repository;

import com.example.livraison.entity.Suivi;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuiviRepository extends JpaRepository<Suivi, Long> {

    List<Suivi> findByLivraisonIdOrderByDateAsc(Long livraisonId);
}
