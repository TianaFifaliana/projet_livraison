package com.example.livraison.service;

import com.example.livraison.dto.request.SuiviRequest;
import com.example.livraison.dto.response.SuiviResponse;
import java.util.List;

public interface SuiviService {

    SuiviResponse add(SuiviRequest request);

    List<SuiviResponse> getByLivraison(Long livraisonId);
}
