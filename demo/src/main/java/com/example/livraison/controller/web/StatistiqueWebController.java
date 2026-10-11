package com.example.livraison.controller.web;

import com.example.livraison.dto.request.PeriodeStatistique;
import com.example.livraison.dto.request.StatistiquesRequest;
import com.example.livraison.service.StatistiqueService;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/statistiques")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
public class StatistiqueWebController {

    private final StatistiqueService statistiqueService;

    public StatistiqueWebController(StatistiqueService statistiqueService) {
        this.statistiqueService = statistiqueService;
    }

    @GetMapping
    public String index(
            @RequestParam(required = false) PeriodeStatistique periode,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin,
            @RequestParam(required = false) Long livreurId,
            @RequestParam(required = false) Long zoneId,
            Model model) {
        StatistiquesRequest request = StatistiquesRequest.builder()
                .periode(periode != null ? periode : PeriodeStatistique.TOUT)
                .dateDebut(dateDebut)
                .dateFin(dateFin)
                .livreurId(livreurId)
                .zoneId(zoneId)
                .build();
        model.addAttribute("stats", statistiqueService.calculerStatistiques(request));
        model.addAttribute("periodes", PeriodeStatistique.values());
        model.addAttribute("active", "statistiques");
        return "statistiques/list";
    }
}
