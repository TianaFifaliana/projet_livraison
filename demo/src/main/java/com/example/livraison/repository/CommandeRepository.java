package com.example.livraison.repository;

import com.example.livraison.entity.Commande;
import com.example.livraison.entity.Priorite;
import com.example.livraison.entity.StatutCommande;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommandeRepository
        extends JpaRepository<Commande, Long>, JpaSpecificationExecutor<Commande> {

    Optional<Commande> findByNumero(String numero);

    boolean existsByNumero(String numero);

    List<Commande> findByClientId(Long clientId);

    List<Commande> findByLivreurId(Long livreurId);

    List<Commande> findByStatut(StatutCommande statut);

    long countByStatut(StatutCommande statut);

    long countByLivreurIdAndStatutIn(Long livreurId, Collection<StatutCommande> statuts);

    @Query("""
            select c from Commande c
            where (:statut is null or c.statut = :statut)
              and (:clientId is null or c.client.id = :clientId)
              and (:livreurId is null or c.livreur.id = :livreurId)
              and (:priorite is null or c.priorite = :priorite)
              and (:dateDebut is null or c.dateCreation >= :dateDebut)
              and (:dateFin is null or c.dateCreation <= :dateFin)
            """)
    Page<Commande> rechercher(@Param("statut") StatutCommande statut,
                              @Param("clientId") Long clientId,
                              @Param("livreurId") Long livreurId,
                              @Param("priorite") Priorite priorite,
                              @Param("dateDebut") LocalDateTime dateDebut,
                              @Param("dateFin") LocalDateTime dateFin,
                              Pageable pageable);
}
