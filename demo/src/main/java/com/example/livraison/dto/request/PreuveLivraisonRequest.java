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
public class PreuveLivraisonRequest {

    @NotNull(message = "La commande est obligatoire")
    private Long commandeId;

    @NotBlank(message = "Le nom du signataire est obligatoire")
    @Size(max = 120, message = "Le nom du signataire ne doit pas depasser 120 caracteres")
    private String nomSignataire;

    @Size(max = 500, message = "Le commentaire ne doit pas depasser 500 caracteres")
    private String commentaire;

    @Size(max = 20, message = "Le code de confirmation ne doit pas depasser 20 caracteres")
    private String codeConfirmation;

    private byte[] signature;

    private byte[] photo;
}