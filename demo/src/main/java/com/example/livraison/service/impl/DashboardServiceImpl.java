package com.example.livraison.service.impl;

import com.example.livraison.dto.response.DashboardStatsResponse;
import com.example.livraison.entity.StatutCommande;
import com.example.livraison.entity.StatutLivraison;
import com.example.livraison.entity.StatutPaiement;
import com.example.livraison.repository.ClientRepository;
import com.example.livraison.repository.CommandeRepository;
import com.example.livraison.repository.LivraisonRepository;
import com.example.livraison.repository.LivreurRepository;
import com.example.livraison.repository.PaiementRepository;
import com.example.livraison.service.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final ClientRepository clientRepository;
    private final LivreurRepository livreurRepository;
    private final CommandeRepository commandeRepository;
    private final LivraisonRepository livraisonRepository;
    private final PaiementRepository paiementRepository;

    public DashboardServiceImpl(ClientRepository clientRepository, LivreurRepository livreurRepository,
                                CommandeRepository commandeRepository, LivraisonRepository livraisonRepository,
                                PaiementRepository paiementRepository) {
        this.clientRepository = clientRepository;
        this.livreurRepository = livreurRepository;
        this.commandeRepository = commandeRepository;
        this.livraisonRepository = livraisonRepository;
        this.paiementRepository = paiementRepository;
    }

    @Override
    public DashboardStatsResponse getStats() {
        return new DashboardStatsResponse(
                clientRepository.count(),
                livreurRepository.count(),
                commandeRepository.count(),
                livraisonRepository.count(),
                commandeRepository.countByStatut(StatutCommande.EN_ATTENTE),
                commandeRepository.countByStatut(StatutCommande.LIVREE),
                livraisonRepository.countByStatut(StatutLivraison.EN_COURS),
                paiementRepository.countByStatut(StatutPaiement.PAYE));
    }
}
