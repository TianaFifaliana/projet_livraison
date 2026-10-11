package com.example.livraison.controller.rest;

import com.example.livraison.dto.request.PeriodeStatistique;
import com.example.livraison.dto.request.StatistiquesRequest;
import com.example.livraison.dto.response.StatistiquesResponse;
import com.example.livraison.service.StatistiqueService;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
public class StatistiqueRestController {

    private final StatistiqueService statistiqueService;

    public StatistiqueRestController(StatistiqueService statistiqueService) {
        this.statistiqueService = statistiqueService;
    }

    @GetMapping
    public ResponseEntity<StatistiquesResponse> getStatistiques(
            @RequestParam(required = false) PeriodeStatistique periode,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin,
            @RequestParam(required = false) Long livreurId,
            @RequestParam(required = false) Long zoneId) {
        StatistiquesRequest request = StatistiquesRequest.builder()
                .periode(periode != null ? periode : PeriodeStatistique.TOUT)
                .dateDebut(dateDebut)
                .dateFin(dateFin)
                .livreurId(livreurId)
                .zoneId(zoneId)
                .build();
        return ResponseEntity.ok(statistiqueService.calculerStatistiques(request));
    }
}
