package com.example.livraison.controller.web;

import com.example.livraison.service.CommandeService;
import com.example.livraison.service.DashboardService;
import com.example.livraison.service.LivraisonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardWebController {

    private final DashboardService dashboardService;
    private final CommandeService commandeService;
    private final LivraisonService livraisonService;

    public DashboardWebController(DashboardService dashboardService, CommandeService commandeService,
                                  LivraisonService livraisonService) {
        this.dashboardService = dashboardService;
        this.commandeService = commandeService;
        this.livraisonService = livraisonService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("stats", dashboardService.getStats());
        model.addAttribute("dernieresCommandes", commandeService.getAll());
        model.addAttribute("dernieresLivraisons", livraisonService.getAll());
        model.addAttribute("active", "dashboard");
        return "dashboard";
    }
}
