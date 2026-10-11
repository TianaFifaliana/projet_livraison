package com.example.livraison.repository;

import com.example.livraison.entity.Paiement;
import com.example.livraison.entity.StatutPaiement;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    Optional<Paiement> findByCommandeId(Long commandeId);

    Optional<Paiement> findByTransactionId(String transactionId);

    List<Paiement> findByStatut(StatutPaiement statut);

    long countByStatut(StatutPaiement statut);

    boolean existsByTransactionId(String transactionId);

    @Query("""
            select coalesce(sum(p.montant), 0) from Paiement p
            join p.commande c
            left join c.livreur lv
            left join c.zone z
            where p.statut = :statut
              and (:livreurId is null or lv.id = :livreurId)
              and (:zoneId is null or z.id = :zoneId)
              and (:dateDebut is null or p.datePaiement >= :dateDebut)
              and (:dateFin is null or p.datePaiement <= :dateFin)
            """)
    BigDecimal sommeMontantParStatut(@Param("statut") StatutPaiement statut,
                                     @Param("livreurId") Long livreurId,
                                     @Param("zoneId") Long zoneId,
                                     @Param("dateDebut") LocalDateTime dateDebut,
                                     @Param("dateFin") LocalDateTime dateFin);
}
