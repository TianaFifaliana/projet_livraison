package com.example.livraison.controller.rest;

import com.example.livraison.dto.request.LivreurRequest;
import com.example.livraison.dto.response.LivreurResponse;
import com.example.livraison.service.LivreurService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/livreurs")
public class LivreurRestController {

    private final LivreurService livreurService;

    public LivreurRestController(LivreurService livreurService) {
        this.livreurService = livreurService;
    }

    @GetMapping
    public ResponseEntity<List<LivreurResponse>> getAll() {
        return ResponseEntity.ok(livreurService.getAll());
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LivreurResponse> create(@Valid @RequestBody LivreurRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(livreurService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LivreurResponse> update(@PathVariable Long id, @Valid @RequestBody LivreurRequest request) {
        return ResponseEntity.ok(livreurService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        livreurService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
