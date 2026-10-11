package com.example.livraison.repository;

import com.example.livraison.entity.Zone;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneRepository extends JpaRepository<Zone, Long> {

    Optional<Zone> findByNom(String nom);

    boolean existsByNom(String nom);
}
