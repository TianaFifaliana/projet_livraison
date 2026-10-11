package com.example.livraison.controller.rest;

import com.example.livraison.dto.request.CreateLivreurRequest;
import com.example.livraison.dto.response.LivreurResponse;
import com.example.livraison.entity.StatutLivreur;
import com.example.livraison.service.LivreurService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/couriers")
public class LivreurRestController {

    private final LivreurService livreurService;

    public LivreurRestController(LivreurService livreurService) {
        this.livreurService = livreurService;
    }

    @GetMapping
    public ResponseEntity<Page<LivreurResponse>> rechercher(
            @RequestParam(required = false) String recherche,
            @RequestParam(required = false) StatutLivreur status,
            @RequestParam(required = false) Long zoneId,
            @PageableDefault(size = 20, sort = "nom", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(livreurService.rechercher(recherche, status, zoneId, pageable));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<LivreurResponse>> getDisponibles() {
        return ResponseEntity.ok(livreurService.getDisponibles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivreurResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(livreurService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<LivreurResponse> create(@Valid @RequestBody CreateLivreurRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(livreurService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<LivreurResponse> update(@PathVariable Long id, @Valid @RequestBody CreateLivreurRequest request) {
        return ResponseEntity.ok(livreurService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','OPERATOR')")
    public ResponseEntity<LivreurResponse> changerStatut(@PathVariable Long id,
                                                         @RequestParam StatutLivreur status) {
        return ResponseEntity.ok(livreurService.changerStatut(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        livreurService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
