package com.example.livraison.controller.rest;

import com.example.livraison.dto.request.CreateCommandeRequest;
import com.example.livraison.dto.request.UpdateCommandeRequest;
import com.example.livraison.dto.request.ValidationGroups;
import com.example.livraison.dto.response.CommandeResponse;
import com.example.livraison.entity.Priorite;
import com.example.livraison.entity.StatutCommande;
import com.example.livraison.service.CommandeService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/api/orders")
public class CommandeRestController {

    private final CommandeService commandeService;

    public CommandeRestController(CommandeService commandeService) {
        this.commandeService = commandeService;
    }

    @GetMapping
    public ResponseEntity<Page<CommandeResponse>> rechercher(
            @RequestParam(required = false) StatutCommande status,
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long livreurId,
            @RequestParam(required = false) Priorite priorite,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin,
            @PageableDefault(size = 20, sort = "dateCreation", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(
                commandeService.rechercher(status, clientId, livreurId, priorite, dateDebut, dateFin, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.getById(id));
    }

    @PostMapping
    public ResponseEntity<CommandeResponse> create(
            @Validated(ValidationGroups.Api.class) @RequestBody CreateCommandeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commandeService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommandeResponse> update(@PathVariable Long id,
                                                   @Valid @RequestBody UpdateCommandeRequest request) {
        return ResponseEntity.ok(commandeService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','OPERATOR')")
    public ResponseEntity<CommandeResponse> changerStatut(@PathVariable Long id,
                                                          @RequestParam StatutCommande status) {
        return ResponseEntity.ok(commandeService.changerStatut(id, status));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','OPERATOR')")
    public ResponseEntity<CommandeResponse> annuler(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.annuler(id));
    }

    @PatchMapping("/{id}/close")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','OPERATOR')")
    public ResponseEntity<CommandeResponse> cloturer(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.cloturer(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commandeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
