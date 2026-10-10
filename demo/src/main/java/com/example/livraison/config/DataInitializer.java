package com.example.livraison.config;

import com.example.livraison.entity.Adresse;
import com.example.livraison.entity.Client;
import com.example.livraison.entity.Colis;
import com.example.livraison.entity.Commande;
import com.example.livraison.entity.Livraison;
import com.example.livraison.entity.Livreur;
import com.example.livraison.entity.MethodePaiement;
import com.example.livraison.entity.Paiement;
import com.example.livraison.entity.Role;
import com.example.livraison.entity.RoleName;
import com.example.livraison.entity.StatutCommande;
import com.example.livraison.entity.StatutLivraison;
import com.example.livraison.entity.StatutPaiement;
import com.example.livraison.entity.Suivi;
import com.example.livraison.entity.User;
import com.example.livraison.repository.AdresseRepository;
import com.example.livraison.repository.ClientRepository;
import com.example.livraison.repository.CommandeRepository;
import com.example.livraison.repository.LivraisonRepository;
import com.example.livraison.repository.LivreurRepository;
import com.example.livraison.repository.PaiementRepository;
import com.example.livraison.repository.RoleRepository;
import com.example.livraison.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final LivreurRepository livreurRepository;
    private final AdresseRepository adresseRepository;
    private final CommandeRepository commandeRepository;
    private final LivraisonRepository livraisonRepository;
    private final PaiementRepository paiementRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.default-admin.username}")
    private String adminUsername;

    @Value("${app.default-admin.password}")
    private String adminPassword;

    @Value("${app.default-admin.email}")
    private String adminEmail;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository,
                           ClientRepository clientRepository, LivreurRepository livreurRepository,
                           AdresseRepository adresseRepository, CommandeRepository commandeRepository,
                           LivraisonRepository livraisonRepository,
                           PaiementRepository paiementRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.livreurRepository = livreurRepository;
        this.adresseRepository = adresseRepository;
        this.commandeRepository = commandeRepository;
        this.livraisonRepository = livraisonRepository;
        this.paiementRepository = paiementRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        initRoles();
        initAdmin();
        initSampleData();
    }

    private void initRoles() {
        for (RoleName roleName : RoleName.values()) {
            roleRepository.findByName(roleName)
                    .orElseGet(() -> roleRepository.save(new Role(roleName)));
        }
    }

    private void initAdmin() {
        if (userRepository.existsByUsername(adminUsername)) {
            return;
        }
        Role adminRole = roleRepository.findByName(RoleName.ADMIN).orElseThrow();
        userRepository.save(User.builder()
                .username(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .email(adminEmail)
                .enabled(true)
                .roles(Set.of(adminRole))
                .build());
    }

    private void initSampleData() {
        if (clientRepository.count() > 0) {
            return;
        }

        Role clientRole = roleRepository.findByName(RoleName.CLIENT).orElseThrow();
        Role livreurRole = roleRepository.findByName(RoleName.LIVREUR).orElseThrow();

        User clientUser = userRepository.save(User.builder()
                .username("client1")
                .password(passwordEncoder.encode("client123"))
                .email("client1@livraison.local")
                .enabled(true)
                .roles(Set.of(clientRole))
                .build());

        Client client = clientRepository.save(Client.builder()
                .nom("Diallo")
                .prenom("Awa")
                .telephone("0600000001")
                .email("client1@livraison.local")
                .user(clientUser)
                .build());

        Adresse adresse1 = adresseRepository.save(Adresse.builder()
                .rue("12 rue des Livreurs")
                .ville("Paris")
                .codePostal("75001")
                .pays("France")
                .latitude(48.8566)
                .longitude(2.3522)
                .client(client)
                .build());

        User livreurUser = userRepository.save(User.builder()
                .username("livreur1")
                .password(passwordEncoder.encode("livreur123"))
                .email("livreur1@livraison.local")
                .enabled(true)
                .roles(Set.of(livreurRole))
                .build());

        Livreur livreur = livreurRepository.save(Livreur.builder()
                .nom("Martin")
                .prenom("Paul")
                .telephone("0600000002")
                .email("livreur1@livraison.local")
                .vehicule("Scooter")
                .disponible(true)
                .user(livreurUser)
                .build());

        Commande commande = Commande.builder()
                .reference("CMD-0001")
                .statut(StatutCommande.CONFIRMEE)
                .montantTotal(new BigDecimal("49.90"))
                .client(client)
                .adresseLivraison(adresse1)
                .build();
        commande.getColis().add(Colis.builder()
                .description("Colis standard")
                .poids(2.5)
                .longueur(30.0)
                .largeur(20.0)
                .hauteur(15.0)
                .fragile(false)
                .commande(commande)
                .build());
        commande = commandeRepository.save(commande);

        Livraison livraison = Livraison.builder()
                .commande(commande)
                .livreur(livreur)
                .dateLivraisonPrevue(LocalDateTime.now().plusDays(1))
                .statut(StatutLivraison.PLANIFIEE)
                .build();
        livraison.getSuivis().add(Suivi.builder()
                .livraison(livraison)
                .statut(StatutLivraison.PLANIFIEE)
                .commentaire("Livraison planifiee")
                .build());
        livraisonRepository.save(livraison);

        paiementRepository.save(Paiement.builder()
                .commande(commande)
                .montant(new BigDecimal("49.90"))
                .methode(MethodePaiement.CARTE_BANCAIRE)
                .statut(StatutPaiement.PAYE)
                .datePaiement(LocalDateTime.now())
                .transactionId("TXN-INIT-0001")
                .build());
    }
}
