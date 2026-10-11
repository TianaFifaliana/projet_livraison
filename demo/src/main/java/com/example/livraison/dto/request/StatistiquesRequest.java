package com.example.livraison.dto.request;

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
public class StatistiquesRequest {

    @Builder.Default
    private PeriodeStatistique periode = PeriodeStatistique.TOUT;

    private LocalDateTime dateDebut;

    private LocalDateTime dateFin;

    private Long livreurId;

    private Long zoneId;
}
