package com.example.livraison.controller.rest;

import com.example.livraison.dto.request.TarifRequest;
import com.example.livraison.dto.response.TarifResponse;
import com.example.livraison.service.TarifService;
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
@RequestMapping("/api/tariffs")
public class TarifRestController {

    private final TarifService tarifService;

    public TarifRestController(TarifService tarifService) {
        this.tarifService = tarifService;
    }

    @GetMapping
    public ResponseEntity<List<TarifResponse>> getAll() {
        return ResponseEntity.ok(tarifService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarifResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(tarifService.getById(id));
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<TarifResponse>> getByZone(@PathVariable Long zoneId) {
        return ResponseEntity.ok(tarifService.getByZone(zoneId));
    }

    @GetMapping("/default")
    public ResponseEntity<TarifResponse> getParDefaut() {
        return ResponseEntity.ok(tarifService.getParDefaut());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TarifResponse> create(@Valid @RequestBody TarifRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tarifService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TarifResponse> update(@PathVariable Long id, @Valid @RequestBody TarifRequest request) {
        return ResponseEntity.ok(tarifService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tarifService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
