package com.example.livraison.dto.response;

import java.time.LocalDateTime;

public record ClientResponse(
        Long id,
        String nom,
        String prenom,
        String telephone,
        String email,
        LocalDateTime dateInscription,
        Long userId) {
}
