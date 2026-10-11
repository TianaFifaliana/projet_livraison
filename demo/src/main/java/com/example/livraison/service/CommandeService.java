package com.example.livraison.service;

import com.example.livraison.dto.request.CreateCommandeRequest;
import com.example.livraison.dto.request.UpdateCommandeRequest;
import com.example.livraison.dto.response.CommandeResponse;
import com.example.livraison.entity.Priorite;
import com.example.livraison.entity.StatutCommande;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommandeService {

    CommandeResponse create(CreateCommandeRequest request);

    CommandeResponse update(Long id, UpdateCommandeRequest request);

    CommandeResponse getById(Long id);

    List<CommandeResponse> getAll();

    List<CommandeResponse> getByClient(Long clientId);

    List<CommandeResponse> getByStatut(StatutCommande statut);

    Page<CommandeResponse> rechercher(StatutCommande statut, Long clientId, Long livreurId, Priorite priorite,
                                      LocalDateTime dateDebut, LocalDateTime dateFin, Pageable pageable);

    CommandeResponse changerStatut(Long id, StatutCommande statut);

    CommandeResponse annuler(Long id);

    CommandeResponse cloturer(Long id);

    void delete(Long id);
}
