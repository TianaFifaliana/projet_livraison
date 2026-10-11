package com.example.livraison.service;

import com.example.livraison.dto.response.CommandeResponse;

public interface AttributionService {

    CommandeResponse attribuerLivreur(Long commandeId);
}
