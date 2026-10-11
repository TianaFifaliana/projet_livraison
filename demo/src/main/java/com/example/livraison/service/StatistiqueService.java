package com.example.livraison.service;

import com.example.livraison.dto.request.StatistiquesRequest;
import com.example.livraison.dto.response.StatistiquesResponse;

public interface StatistiqueService {

    StatistiquesResponse calculerStatistiques(StatistiquesRequest request);
}
