package com.example.livraison.dto.request;

import com.example.livraison.entity.StatutCommande;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandeRequest {

    @NotNull(message = "Le client est obligatoire")
    private Long clientId;

    @NotNull(message = "L'adresse de livraison est obligatoire")
    private Long adresseId;

    @Builder.Default
    private StatutCommande statut = StatutCommande.EN_ATTENTE;

    @NotNull(message = "Le montant total est obligatoire")
    @DecimalMin(value = "0.0", inclusive = true, message = "Le montant doit etre positif")
    private BigDecimal montantTotal;

    @Builder.Default
    @Valid
    private List<ColisRequest> colis = new ArrayList<>();
}
