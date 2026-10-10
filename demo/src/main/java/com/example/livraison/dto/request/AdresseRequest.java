package com.example.livraison.dto.request;

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
public class AdresseRequest {

    @NotBlank(message = "La rue est obligatoire")
    @Size(max = 150)
    private String rue;

    @NotBlank(message = "La ville est obligatoire")
    @Size(max = 80)
    private String ville;

    @NotBlank(message = "Le code postal est obligatoire")
    @Size(max = 20)
    private String codePostal;

    @NotBlank(message = "Le pays est obligatoire")
    @Size(max = 80)
    private String pays;

    private Double latitude;

    private Double longitude;

    @NotNull(message = "Le client est obligatoire")
    private Long clientId;
}
