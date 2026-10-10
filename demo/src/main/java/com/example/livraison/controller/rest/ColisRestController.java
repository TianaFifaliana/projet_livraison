package com.example.livraison.controller.rest;

import com.example.livraison.dto.request.ColisRequest;
import com.example.livraison.dto.response.ColisResponse;
import com.example.livraison.service.ColisService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/colis")
public class ColisRestController {

    private final ColisService colisService;

    public ColisRestController(ColisService colisService) {
        this.colisService = colisService;
    }

    @GetMapping
    public ResponseEntity<List<ColisResponse>> getAll() {
        return ResponseEntity.ok(colisService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColisResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(colisService.getById(id));
    }

    @GetMapping("/commande/{commandeId}")
    public ResponseEntity<List<ColisResponse>> getByCommande(@PathVariable Long commandeId) {
        return ResponseEntity.ok(colisService.getByCommande(commandeId));
    }

    @PostMapping
    public ResponseEntity<ColisResponse> create(@Valid @RequestBody ColisRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(colisService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColisResponse> update(@PathVariable Long id, @Valid @RequestBody ColisRequest request) {
        return ResponseEntity.ok(colisService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        colisService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
