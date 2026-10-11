package com.example.livraison.controller.rest;

import com.example.livraison.dto.request.CreateIncidentRequest;
import com.example.livraison.dto.request.ResoudreIncidentRequest;
import com.example.livraison.dto.response.IncidentResponse;
import com.example.livraison.service.IncidentService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/incidents")
public class IncidentRestController {

    private final IncidentService incidentService;

    public IncidentRestController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping
    public ResponseEntity<List<IncidentResponse>> getAll() {
        return ResponseEntity.ok(incidentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.getById(id));
    }

    @GetMapping("/commande/{commandeId}")
    public ResponseEntity<List<IncidentResponse>> getByCommande(@PathVariable Long commandeId) {
        return ResponseEntity.ok(incidentService.getByCommande(commandeId));
    }

    @GetMapping("/courier/{livreurId}")
    public ResponseEntity<List<IncidentResponse>> getByLivreur(@PathVariable Long livreurId) {
        return ResponseEntity.ok(incidentService.getByLivreur(livreurId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','OPERATOR')")
    public ResponseEntity<IncidentResponse> signaler(@Valid @RequestBody CreateIncidentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(incidentService.signaler(request));
    }

    @PatchMapping("/{id}/resolution")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','OPERATOR')")
    public ResponseEntity<IncidentResponse> resoudre(@PathVariable Long id,
                                                     @Valid @RequestBody ResoudreIncidentRequest request) {
        return ResponseEntity.ok(incidentService.resoudre(id, request.getCommentaireResolution()));
    }
}
