package com.example.livraison.repository;

import com.example.livraison.entity.Livraison;
import com.example.livraison.entity.StatutLivraison;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LivraisonRepository extends JpaRepository<Livraison, Long> {

    Optional<Livraison> findByCommandeId(Long commandeId);

    List<Livraison> findByLivreurId(Long livreurId);

    List<Livraison> findByStatut(StatutLivraison statut);

    long countByStatut(StatutLivraison statut);

    @Query("""
            select l from Livraison l
            join l.commande c
            left join l.livreur lv
            left join c.zone z
            where (:livreurId is null or lv.id = :livreurId)
              and (:zoneId is null or z.id = :zoneId)
              and (:dateDebut is null or coalesce(l.dateLivraisonReelle, l.dateLivraisonPrevue) >= :dateDebut)
              and (:dateFin is null or coalesce(l.dateLivraisonReelle, l.dateLivraisonPrevue) <= :dateFin)
            """)
    List<Livraison> rechercherPourStatistiques(@Param("livreurId") Long livreurId,
                                               @Param("zoneId") Long zoneId,
                                               @Param("dateDebut") LocalDateTime dateDebut,
                                               @Param("dateFin") LocalDateTime dateFin);
}
