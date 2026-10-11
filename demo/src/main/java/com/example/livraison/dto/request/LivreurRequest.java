package com.example.livraison.dto.request;

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
public class LivreurRequest {

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 60)
    private String nom;

    @NotBlank(message = "Le prenom est obligatoire")
    @Size(max = 60)
    private String prenom;

    @NotBlank(message = "Le telephone est obligatoire")
    @Size(max = 20)
    private String telephone;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit etre valide")
    private String email;

    @Size(max = 40)
    private String vehicule;

    private Long userId;
}
