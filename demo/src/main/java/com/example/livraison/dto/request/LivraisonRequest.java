package com.example.livraison.dto.request;

import com.example.livraison.entity.StatutLivraison;
import jakarta.validation.constraints.NotNull;
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
public class LivraisonRequest {

    @NotNull(message = "La commande est obligatoire")
    private Long commandeId;

    private Long livreurId;

    private LocalDateTime dateLivraisonPrevue;

    @Builder.Default
    private StatutLivraison statut = StatutLivraison.PLANIFIEE;
}
