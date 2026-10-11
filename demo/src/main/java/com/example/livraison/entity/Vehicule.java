package com.example.livraison.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vehicules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String immatriculation;

    @Column(length = 60)
    private String marque;

    @Column(length = 60)
    private String modele;

    @Column
    private Double capacitePoids;

    @Column
    private Double capaciteVolume;

    @Builder.Default
    @Column(nullable = false)
    private boolean disponible = true;

    @OneToOne(mappedBy = "vehicule", fetch = FetchType.LAZY)
    private Livreur livreur;
}
