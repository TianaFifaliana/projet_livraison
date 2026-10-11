package com.example.livraison.repository;

import com.example.livraison.entity.Tarif;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarifRepository extends JpaRepository<Tarif, Long> {

    List<Tarif> findByZoneId(Long zoneId);

    Optional<Tarif> findFirstByZoneIdOrderByIdAsc(Long zoneId);

    List<Tarif> findByParDefautTrue();

    Optional<Tarif> findFirstByParDefautTrueOrderByIdAsc();
}
