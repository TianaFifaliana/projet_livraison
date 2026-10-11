package com.example.livraison.controller.rest;

import com.example.livraison.dto.request.LivraisonRequest;
import com.example.livraison.dto.request.SuiviRequest;
import com.example.livraison.dto.response.LivraisonResponse;
import com.example.livraison.dto.response.SuiviResponse;
import com.example.livraison.entity.StatutLivraison;
import com.example.livraison.service.LivraisonService;
import com.example.livraison.service.SuiviService;
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
@RequestMapping("/api/livraisons")
public class LivraisonRestController {

    private final LivraisonService livraisonService;
    private final SuiviService suiviService;

    public LivraisonRestController(LivraisonService livraisonService, SuiviService suiviService) {
        this.livraisonService = livraisonService;
        this.suiviService = suiviService;
    }

    @GetMapping
    public ResponseEntity<List<LivraisonResponse>> getAll() {
        return ResponseEntity.ok(livraisonService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivraisonResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(livraisonService.getById(id));
    }

    @GetMapping("/livreur/{livreurId}")
    public ResponseEntity<List<LivraisonResponse>> getByLivreur(@PathVariable Long livreurId) {
        return ResponseEntity.ok(livraisonService.getByLivreur(livreurId));
    }

    @GetMapping("/{id}/suivis")
    public ResponseEntity<List<SuiviResponse>> getSuivis(@PathVariable Long id) {
        return ResponseEntity.ok(suiviService.getByLivraison(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','OPERATOR')")
    public ResponseEntity<LivraisonResponse> create(@Valid @RequestBody LivraisonRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(livraisonService.create(request));
    }

    @PutMapping("/{id}/livreur/{livreurId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','OPERATOR')")
    public ResponseEntity<LivraisonResponse> assignerLivreur(@PathVariable Long id, @PathVariable Long livreurId) {
        return ResponseEntity.ok(livraisonService.assignerLivreur(id, livreurId));
    }

    @PutMapping("/{id}/statut")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','OPERATOR')")
    public ResponseEntity<LivraisonResponse> updateStatut(@PathVariable Long id, @RequestParam StatutLivraison statut) {
        return ResponseEntity.ok(livraisonService.updateStatut(id, statut));
    }

    @PostMapping("/suivis")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','OPERATOR')")
    public ResponseEntity<SuiviResponse> addSuivi(@Valid @RequestBody SuiviRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(suiviService.add(request));
    }
}
