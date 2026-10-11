package com.example.livraison.repository;

import com.example.livraison.entity.Utilisateur;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    Optional<Utilisateur> findByEmail(String email);

    Optional<Utilisateur> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
