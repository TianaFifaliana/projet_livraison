package com.example.livraison.controller.rest;

import com.example.livraison.dto.request.PaiementRequest;
import com.example.livraison.dto.response.PaiementResponse;
import com.example.livraison.entity.StatutPaiement;
import com.example.livraison.service.PaiementService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/paiements")
@PreAuthorize("hasRole('ADMIN')")
public class PaiementRestController {

    private final PaiementService paiementService;

    public PaiementRestController(PaiementService paiementService) {
        this.paiementService = paiementService;
    }

    @GetMapping
    public ResponseEntity<List<PaiementResponse>> getAll() {
        return ResponseEntity.ok(paiementService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaiementResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(paiementService.getById(id));
    }

    @GetMapping("/commande/{commandeId}")
    public ResponseEntity<PaiementResponse> getByCommande(@PathVariable Long commandeId) {
        return ResponseEntity.ok(paiementService.getByCommande(commandeId));
    }

    @PostMapping
    public ResponseEntity<PaiementResponse> create(@Valid @RequestBody PaiementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paiementService.create(request));
    }

    @PutMapping("/{id}/statut")
    public ResponseEntity<PaiementResponse> updateStatut(@PathVariable Long id, @RequestParam StatutPaiement statut) {
        return ResponseEntity.ok(paiementService.updateStatut(id, statut));
    }
}
