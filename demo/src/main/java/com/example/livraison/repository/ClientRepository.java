package com.example.livraison.repository;

import com.example.livraison.entity.Client;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);

    Optional<Client> findByUtilisateurId(Long utilisateurId);

    boolean existsByEmail(String email);

    @Query("""
            select c from Client c
            where :recherche is null
               or lower(c.nom) like lower(concat('%', :recherche, '%'))
               or lower(c.prenom) like lower(concat('%', :recherche, '%'))
               or lower(c.email) like lower(concat('%', :recherche, '%'))
               or lower(c.telephone) like lower(concat('%', :recherche, '%'))
            """)
    Page<Client> rechercher(@Param("recherche") String recherche, Pageable pageable);
}
