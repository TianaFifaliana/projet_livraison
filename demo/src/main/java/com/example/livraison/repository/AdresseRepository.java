package com.example.livraison.repository;

import com.example.livraison.entity.Adresse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdresseRepository extends JpaRepository<Adresse, Long> {

    List<Adresse> findByClientId(Long clientId);

    List<Adresse> findByVilleIgnoreCase(String ville);
}
