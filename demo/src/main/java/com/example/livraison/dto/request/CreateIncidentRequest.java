package com.example.livraison.dto.request;

import com.example.livraison.entity.TypeIncident;
import jakarta.validation.constraints.NotBlank;
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
public class CreateIncidentRequest {

    @NotNull(message = "Le type d'incident est obligatoire")
    private TypeIncident type;

    @NotBlank(message = "La description est obligatoire")
    @Size(max = 500, message = "La description ne doit pas depasser 500 caracteres")
    private String description;

    @NotNull(message = "La commande est obligatoire")
    private Long commandeId;

    @NotNull(message = "Le livreur est obligatoire")
    private Long livreurId;
}