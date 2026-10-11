package com.example.livraison.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
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
public class TarifRequest {

    @NotBlank(message = "Le libelle est obligatoire")
    @Size(max = 100, message = "Le libelle ne doit pas depasser 100 caracteres")
    private String libelle;

    @NotNull(message = "Le prix de base est obligatoire")
    @PositiveOrZero(message = "Le prix de base doit etre positif")
    private BigDecimal prixBase;

    @NotNull(message = "Le prix par km est obligatoire")
    @PositiveOrZero(message = "Le prix par km doit etre positif")
    private BigDecimal prixParKm;

    @PositiveOrZero(message = "Le prix par kg doit etre positif")
    private BigDecimal prixParKg;

    @PositiveOrZero(message = "Le prix par m3 doit etre positif")
    private BigDecimal prixParM3;

    @PositiveOrZero(message = "Le coefficient de zone doit etre positif")
    @Builder.Default
    private BigDecimal coefficientZone = BigDecimal.ONE;

    @Builder.Default
    private boolean parDefaut = false;

    @Positive(message = "Le poids maximum doit etre positif")
    private Double poidsMax;

    private Long zoneId;
}
