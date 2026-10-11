package com.example.livraison.dto.request;

import com.example.livraison.entity.StatutLivreur;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class CreateLivreurRequest {

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 60, message = "Le nom ne doit pas depasser 60 caracteres")
    private String nom;

    @NotBlank(message = "Le prenom est obligatoire")
    @Size(max = 60, message = "Le prenom ne doit pas depasser 60 caracteres")
    private String prenom;

    @NotBlank(message = "Le telephone est obligatoire")
    @Size(max = 20, message = "Le telephone ne doit pas depasser 20 caracteres")
    private String telephone;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit etre valide")
    @Size(max = 120, message = "L'email ne doit pas depasser 120 caracteres")
    private String email;

    @Builder.Default
    private StatutLivreur statut = StatutLivreur.DISPONIBLE;

    private Long zoneId;

    private Long vehiculeId;

    private Long utilisateurId;
}