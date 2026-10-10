package com.example.livraison.dto.response;

public record DashboardStatsResponse(
        long totalClients,
        long totalLivreurs,
        long totalCommandes,
        long totalLivraisons,
        long commandesEnAttente,
        long commandesLivrees,
        long livraisonsEnCours,
        long paiementsPayes) {
}
