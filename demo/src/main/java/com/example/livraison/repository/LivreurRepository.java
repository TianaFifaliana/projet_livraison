package com.example.livraison.repository;

import com.example.livraison.entity.Livreur;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivreurRepository extends JpaRepository<Livreur, Long> {

    Optional<Livreur> findByEmail(String email);

    Optional<Livreur> findByUserId(Long userId);

    List<Livreur> findByDisponibleTrue();

    boolean existsByEmail(String email);
}
