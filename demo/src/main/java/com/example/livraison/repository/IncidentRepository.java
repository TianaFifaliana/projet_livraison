package com.example.livraison.repository;

import com.example.livraison.entity.Incident;
import com.example.livraison.entity.TypeIncident;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentRepository extends JpaRepository<Incident, Long> {

    List<Incident> findByCommandeId(Long commandeId);

    List<Incident> findByLivreurId(Long livreurId);

    List<Incident> findByType(TypeIncident type);
}
