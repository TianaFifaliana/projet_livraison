package com.example.livraison.entity;

import java.math.BigDecimal;

public enum Priorite {
    BASSE(new BigDecimal("0.90")),
    NORMALE(new BigDecimal("1.00")),
    HAUTE(new BigDecimal("1.25")),
    URGENTE(new BigDecimal("1.50"));

    private final BigDecimal coefficient;

    Priorite(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }

    public BigDecimal getCoefficient() {
        return coefficient;
    }
}
