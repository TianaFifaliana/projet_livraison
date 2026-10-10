package com.example.livraison.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "livreurs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Livreur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String nom;

    @Column(nullable = false, length = 60)
    private String prenom;

    @Column(nullable = false, length = 20)
    private String telephone;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(length = 40)
    private String vehicule;

    @Builder.Default
    @Column(nullable = false)
    private boolean disponible = true;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "livreur")
    private List<Livraison> livraisons = new ArrayList<>();
}
