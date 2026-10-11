package com.example.livraison.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "zones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nom;

    @Column(length = 255)
    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "zone")
    private List<Livreur> livreurs = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "zone")
    private List<Commande> commandes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "zone")
    private List<Tarif> tarifs = new ArrayList<>();
}
