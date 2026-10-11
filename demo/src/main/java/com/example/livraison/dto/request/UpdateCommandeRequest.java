package com.example.livraison.dto.request;

import com.example.livraison.entity.Priorite;
import com.example.livraison.entity.StatutCommande;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class UpdateCommandeRequest {

    private Long clientId;

    @NotBlank(message = "L'adresse d'enlevement est obligatoire")
    @Size(max = 255, message = "L'adresse d'enlevement ne doit pas depasser 255 caracteres")
    private String adresseEnlevement;

    @NotBlank(message = "L'adresse de livraison est obligatoire")
    @Size(max = 255, message = "L'adresse de livraison ne doit pas depasser 255 caracteres")
    private String adresseLivraison;

    @Positive(message = "Le poids doit etre positif")
    private Double poids;

    @Positive(message = "Le volume doit etre positif")
    private Double volume;

    @NotNull(message = "La priorite est obligatoire")
    private Priorite priorite;

    @Positive(message = "La distance doit etre positive")
    private Double distance;

    private BigDecimal prix;

    private LocalDateTime datePrevue;

    private StatutCommande statut;

    private Long livreurId;

    private Long zoneId;
}