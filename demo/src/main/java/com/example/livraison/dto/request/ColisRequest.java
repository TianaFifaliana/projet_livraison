package com.example.livraison.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
public class ColisRequest {

    @NotBlank(message = "La description est obligatoire")
    @Size(max = 200)
    private String description;

    @NotNull(message = "Le poids est obligatoire")
    @Positive(message = "Le poids doit etre positif")
    private Double poids;

    @Positive(message = "Le volume doit etre positif")
    private Double volume;

    @Builder.Default
    private boolean fragile = false;

    private Long commandeId;
}
