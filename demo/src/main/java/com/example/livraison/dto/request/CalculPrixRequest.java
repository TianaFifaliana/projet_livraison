package com.example.livraison.dto.request;

import com.example.livraison.entity.Priorite;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
public class CalculPrixRequest {

    @PositiveOrZero(message = "La distance doit etre positive")
    private Double distance;

    @PositiveOrZero(message = "Le poids doit etre positif")
    private Double poids;

    @PositiveOrZero(message = "Le volume doit etre positif")
    private Double volume;

    @NotNull(message = "La priorite est obligatoire")
    @Builder.Default
    private Priorite priorite = Priorite.NORMALE;

    private Long zoneId;
}
