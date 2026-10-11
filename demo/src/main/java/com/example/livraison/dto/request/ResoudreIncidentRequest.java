package com.example.livraison.dto.request;

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
public class ResoudreIncidentRequest {

    @Size(max = 500, message = "Le commentaire de resolution ne doit pas depasser 500 caracteres")
    private String commentaireResolution;
}
