package com.example.livraison.controller.rest;

import com.example.livraison.dto.request.CommandeRequest;
import com.example.livraison.dto.response.CommandeResponse;
import com.example.livraison.entity.StatutCommande;
import com.example.livraison.service.CommandeService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/commandes")
public class CommandeRestController {

    private final CommandeService commandeService;

    public CommandeRestController(CommandeService commandeService) {
        this.commandeService = commandeService;
    }

    @GetMapping
    public ResponseEntity<List<CommandeResponse>> getAll() {
        return ResponseEntity.ok(commandeService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.getById(id));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<CommandeResponse>> getByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(commandeService.getByClient(clientId));
    }

    @GetMapping("/statut")
    public ResponseEntity<List<CommandeResponse>> getByStatut(@RequestParam StatutCommande statut) {
        return ResponseEntity.ok(commandeService.getByStatut(statut));
    }

    @PostMapping
    public ResponseEntity<CommandeResponse> create(@Valid @RequestBody CommandeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commandeService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommandeResponse> update(@PathVariable Long id, @Valid @RequestBody CommandeRequest request) {
        return ResponseEntity.ok(commandeService.update(id, request));
    }

    @PutMapping("/{id}/statut")
    public ResponseEntity<CommandeResponse> updateStatut(@PathVariable Long id, @RequestParam StatutCommande statut) {
        return ResponseEntity.ok(commandeService.updateStatut(id, statut));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commandeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
