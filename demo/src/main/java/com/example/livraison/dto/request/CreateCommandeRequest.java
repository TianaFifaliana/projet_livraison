package com.example.livraison.dto.request;

import com.example.livraison.entity.Priorite;
import com.example.livraison.entity.StatutCommande;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
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
public class CreateCommandeRequest {

    @NotNull(message = "Le client est obligatoire", groups = ValidationGroups.Api.class)
    private Long clientId;

    @NotBlank(message = "Le nom du client est obligatoire", groups = ValidationGroups.Web.class)
    @Size(max = 60, message = "Le nom ne doit pas depasser 60 caracteres", groups = ValidationGroups.Web.class)
    private String clientNom;

    @NotBlank(message = "Le prenom du client est obligatoire", groups = ValidationGroups.Web.class)
    @Size(max = 60, message = "Le prenom ne doit pas depasser 60 caracteres", groups = ValidationGroups.Web.class)
    private String clientPrenom;

    @NotBlank(message = "Le telephone du client est obligatoire", groups = ValidationGroups.Web.class)
    @Size(max = 20, message = "Le telephone ne doit pas depasser 20 caracteres", groups = ValidationGroups.Web.class)
    private String clientTelephone;

    @NotBlank(message = "L'email du client est obligatoire", groups = ValidationGroups.Web.class)
    @Email(message = "L'email du client doit etre valide", groups = ValidationGroups.Web.class)
    @Size(max = 120, message = "L'email ne doit pas depasser 120 caracteres", groups = ValidationGroups.Web.class)
    private String clientEmail;

    @Size(max = 255, message = "L'adresse ne doit pas depasser 255 caracteres", groups = ValidationGroups.Web.class)
    private String clientAdresse;

    @NotBlank(message = "L'adresse d'enlevement est obligatoire")
    @Size(max = 255, message = "L'adresse d'enlevement ne doit pas depasser 255 caracteres")
    private String adresseEnlevement;

    @NotBlank(message = "L'adresse de livraison est obligatoire")
    @Size(max = 255, message = "L'adresse de livraison ne doit pas depasser 255 caracteres")
    private String adresseLivraison;

    @Positive(message = "Le poids doit etre positif")
    private Double poids;

    @Positive(message = "Le volume doit etre positif")
    private Double volume;

    @NotNull(message = "La priorite est obligatoire")
    @Builder.Default
    private Priorite priorite = Priorite.NORMALE;

    @Positive(message = "La distance doit etre positive")
    private Double distance;

    private BigDecimal prix;

    private LocalDateTime datePrevue;

    @Builder.Default
    private StatutCommande statut = StatutCommande.CREEE;

    private Long livreurId;

    private Long zoneId;
}