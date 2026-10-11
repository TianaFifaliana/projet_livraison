package com.example.livraison.repository;

import com.example.livraison.entity.Livreur;
import com.example.livraison.entity.StatutLivreur;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LivreurRepository extends JpaRepository<Livreur, Long> {

    List<Livreur> findByStatutAndZoneId(StatutLivreur statut, Long zoneId);

    Optional<Livreur> findByEmail(String email);

    Optional<Livreur> findByUtilisateurId(Long utilisateurId);

    List<Livreur> findByStatut(StatutLivreur statut);

    List<Livreur> findByZoneId(Long zoneId);

    boolean existsByEmail(String email);

    @Query("""
            select l from Livreur l
            where (:statut is null or l.statut = :statut)
              and (:zoneId is null or l.zone.id = :zoneId)
              and (:recherche is null
                   or lower(l.nom) like lower(concat('%', :recherche, '%'))
                   or lower(l.prenom) like lower(concat('%', :recherche, '%'))
                   or lower(l.email) like lower(concat('%', :recherche, '%'))
                   or lower(l.telephone) like lower(concat('%', :recherche, '%')))
            """)
    Page<Livreur> rechercher(@Param("recherche") String recherche,
                             @Param("statut") StatutLivreur statut,
                             @Param("zoneId") Long zoneId,
                             Pageable pageable);
}
