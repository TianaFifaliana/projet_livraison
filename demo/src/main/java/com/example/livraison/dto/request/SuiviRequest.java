package com.example.livraison.dto.request;

import com.example.livraison.entity.StatutLivraison;
import jakarta.validation.constraints.NotNull;
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
public class SuiviRequest {

    @NotNull(message = "La livraison est obligatoire")
    private Long livraisonId;

    @NotNull(message = "Le statut est obligatoire")
    private StatutLivraison statut;

    @Size(max = 255)
    private String commentaire;

    @Size(max = 150)
    private String position;
}
