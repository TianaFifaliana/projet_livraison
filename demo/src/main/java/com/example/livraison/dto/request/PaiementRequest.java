package com.example.livraison.dto.request;

import com.example.livraison.entity.MethodePaiement;
import com.example.livraison.entity.StatutPaiement;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
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
public class PaiementRequest {

    @NotNull(message = "La commande est obligatoire")
    private Long commandeId;

    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.0", inclusive = true, message = "Le montant doit etre positif")
    private BigDecimal montant;

    @NotNull(message = "La methode de paiement est obligatoire")
    private MethodePaiement methode;

    @Builder.Default
    private StatutPaiement statut = StatutPaiement.EN_ATTENTE;

    @Size(max = 80)
    private String transactionId;
}
