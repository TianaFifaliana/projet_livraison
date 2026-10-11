package com.example.livraison.config;

import com.example.livraison.entity.Client;
import com.example.livraison.entity.Commande;
import com.example.livraison.entity.Livraison;
import com.example.livraison.entity.Livreur;
import com.example.livraison.entity.MethodePaiement;
import com.example.livraison.entity.Paiement;
import com.example.livraison.entity.PreuveLivraison;
import com.example.livraison.entity.Priorite;
import com.example.livraison.entity.Role;
import com.example.livraison.entity.StatutCommande;
import com.example.livraison.entity.StatutLivraison;
import com.example.livraison.entity.StatutLivreur;
import com.example.livraison.entity.StatutPaiement;
import com.example.livraison.entity.Suivi;
import com.example.livraison.entity.Tarif;
import com.example.livraison.entity.Utilisateur;
import com.example.livraison.entity.Vehicule;
import com.example.livraison.entity.Zone;
import com.example.livraison.repository.ClientRepository;
import com.example.livraison.repository.CommandeRepository;
import com.example.livraison.repository.LivraisonRepository;
import com.example.livraison.repository.LivreurRepository;
import com.example.livraison.repository.PaiementRepository;
import com.example.livraison.repository.PreuveLivraisonRepository;
import com.example.livraison.repository.TarifRepository;
import com.example.livraison.repository.UtilisateurRepository;
import com.example.livraison.repository.VehiculeRepository;
import com.example.livraison.repository.ZoneRepository;
import com.example.livraison.service.PrixService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final ClientRepository clientRepository;
    private final LivreurRepository livreurRepository;
    private final ZoneRepository zoneRepository;
    private final TarifRepository tarifRepository;
    private final VehiculeRepository vehiculeRepository;
    private final CommandeRepository commandeRepository;
    private final LivraisonRepository livraisonRepository;
    private final PaiementRepository paiementRepository;
    private final PreuveLivraisonRepository preuveLivraisonRepository;
    private final PrixService prixService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.default-admin.username:admin}")
    private String adminUsername;

    @Value("${app.default-admin.password:admin123}")
    private String adminPassword;

    @Value("${app.default-admin.email:admin@livraison.local}")
    private String adminEmail;

    @Value("${app.default-manager.username:manager}")
    private String managerUsername;

    @Value("${app.default-manager.password:manager123}")
    private String managerPassword;

    @Value("${app.default-manager.email:manager@livraison.local}")
    private String managerEmail;

    @Value("${app.default-operator.username:operator}")
    private String operatorUsername;

    @Value("${app.default-operator.password:operator123}")
    private String operatorPassword;

    @Value("${app.default-operator.email:operator@livraison.local}")
    private String operatorEmail;

    @Value("${app.default-user.username:user}")
    private String userUsername;

    @Value("${app.default-user.password:user123}")
    private String userPassword;

    @Value("${app.default-user.email:user@livraison.local}")
    private String userEmail;

    public DataInitializer(UtilisateurRepository utilisateurRepository, ClientRepository clientRepository,
                           LivreurRepository livreurRepository, ZoneRepository zoneRepository,
                           TarifRepository tarifRepository, VehiculeRepository vehiculeRepository,
                           CommandeRepository commandeRepository, LivraisonRepository livraisonRepository,
                           PaiementRepository paiementRepository,
                           PreuveLivraisonRepository preuveLivraisonRepository,
                           PrixService prixService, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.clientRepository = clientRepository;
        this.livreurRepository = livreurRepository;
        this.zoneRepository = zoneRepository;
        this.tarifRepository = tarifRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.commandeRepository = commandeRepository;
        this.livraisonRepository = livraisonRepository;
        this.paiementRepository = paiementRepository;
        this.preuveLivraisonRepository = preuveLivraisonRepository;
        this.prixService = prixService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        creerUtilisateur(adminUsername, adminPassword, adminEmail, Role.ADMIN);
        creerUtilisateur(managerUsername, managerPassword, managerEmail, Role.MANAGER);
        creerUtilisateur(operatorUsername, operatorPassword, operatorEmail, Role.OPERATOR);
        creerUtilisateur(userUsername, userPassword, userEmail, Role.USER);

        if (zoneRepository.count() == 0) {
            initialiserDonneesDemo();
        }
    }

    private void initialiserDonneesDemo() {
        Zone centre = creerZone("Centre", "Zone urbaine Centrale");
        Zone nord = creerZone("Nord", "Zone suburbaine Nord");

        creerTarifs(centre, nord);

        Vehicule vehiculeMoto = creerVehicule("AA-123-BB", "Yamaha", "NMAX", 50.0, 0.5);
        Vehicule vehiculeFourgon = creerVehicule("CC-456-DD", "Peugeot", "Partner", 250.0, 2.5);
        Vehicule vehiculeCamion = creerVehicule("EE-789-FF", "Renault", "Master", 600.0, 6.0);

        Client awa = creerClient("client1", "client123", "client1@demo.local",
                "Diallo", "Awa", "0611223344", "Dakar, Plateau");
        Client karim = creerClient("client2", "client123", "client2@demo.local",
                "Ndiaye", "Karim", "0622334455", "Thies, Centre ville");

        Livreur moussa = creerLivreur("livreur1", "livreur123", "livreur.dakar@demo.local",
                "Sow", "Moussa", "0633445566", vehiculeFourgon, centre, StatutLivreur.DISPONIBLE);
        Livreur aminata = creerLivreur("livreur2", "livreur123", "livreur.thies@demo.local",
                "Fall", "Aminata", "0644556677", vehiculeCamion, centre, StatutLivreur.DISPONIBLE);
        Livreur ibrahima = creerLivreur("livreur3", "livreur123", "livreur.nord@demo.local",
                "Ba", "Ibrahima", "0655667788", vehiculeMoto, nord, StatutLivreur.EN_LIVRAISON);

        Commande c1 = creerCommande("CMD-DEMO-1001", awa, centre, moussa, 8.5, 12.0, 1.5,
                Priorite.HAUTE, StatutCommande.ASSIGNEE, 1);
        Commande c2 = creerCommande("CMD-DEMO-1002", karim, centre, null, 5.0, 8.0, 1.0,
                Priorite.NORMALE, StatutCommande.CREEE, 1);
        Commande c3 = creerCommande("CMD-DEMO-1003", awa, nord, ibrahima, 20.0, 30.0, 4.0,
                Priorite.URGENTE, StatutCommande.EN_TRANSIT, 1);
        Commande c4 = creerCommande("CMD-DEMO-1004", karim, centre, aminata, 12.0, 3.0, 0.5,
                Priorite.NORMALE, StatutCommande.PRISE_EN_CHARGE, 1);
        Commande c5 = creerCommande("CMD-DEMO-1005", awa, centre, aminata, 4.2, 2.0, 0.3,
                Priorite.BASSE, StatutCommande.LIVREE, 1);
        Commande c6 = creerCommande("CMD-DEMO-1006", karim, centre, null, 15.0, 7.0, 1.2,
                Priorite.NORMALE, StatutCommande.CREEE, 2);

        Livraison livraison1 = creerLivraison(c1, moussa, StatutLivraison.ASSIGNEE);
        creerSuivi(livraison1, StatutLivraison.PLANIFIEE, "Livraison planifiee");
        creerSuivi(livraison1, StatutLivraison.ASSIGNEE, "Livreur assigne : " + moussa.getPrenom());

        Livraison livraison2 = creerLivraison(c3, ibrahima, StatutLivraison.EN_COURS);
        creerSuivi(livraison2, StatutLivraison.PLANIFIEE, "Livraison planifiee");
        creerSuivi(livraison2, StatutLivraison.EN_COURS, "En route vers le destinataire");

        Livraison livraison3 = creerLivraison(c5, aminata, StatutLivraison.LIVREE);
        creerSuivi(livraison3, StatutLivraison.LIVREE, "Colis remis au destinataire");

        creerPreuveLivraison(c5);
        creerPaiement(c5);
    }

    private Zone creerZone(String nom, String description) {
        if (zoneRepository.existsByNom(nom)) {
            return zoneRepository.findByNom(nom).orElseThrow();
        }
        return zoneRepository.save(Zone.builder().nom(nom).description(description).build());
    }

    private void creerTarifs(Zone centre, Zone nord) {
        tarifRepository.save(Tarif.builder()
                .libelle("Tarif par defaut")
                .prixBase(new BigDecimal("5.00"))
                .prixParKm(new BigDecimal("0.50"))
                .prixParKg(new BigDecimal("0.20"))
                .prixParM3(new BigDecimal("1.00"))
                .parDefaut(true)
                .build());
        tarifRepository.save(Tarif.builder()
                .libelle("Tarif zone Centre")
                .prixBase(new BigDecimal("6.00"))
                .prixParKm(new BigDecimal("0.60"))
                .prixParKg(new BigDecimal("0.25"))
                .prixParM3(new BigDecimal("1.20"))
                .coefficientZone(new BigDecimal("1.00"))
                .zone(centre)
                .build());
        tarifRepository.save(Tarif.builder()
                .libelle("Tarif zone Nord")
                .prixBase(new BigDecimal("5.50"))
                .prixParKm(new BigDecimal("0.55"))
                .prixParKg(new BigDecimal("0.20"))
                .prixParM3(new BigDecimal("1.00"))
                .coefficientZone(new BigDecimal("1.05"))
                .zone(nord)
                .build());
    }

    private Vehicule creerVehicule(String immatriculation, String marque, String modele,
                                   Double capacitePoids, Double capaciteVolume) {
        if (vehiculeRepository.existsByImmatriculation(immatriculation)) {
            return vehiculeRepository.findByImmatriculation(immatriculation).orElseThrow();
        }
        return vehiculeRepository.save(Vehicule.builder()
                .immatriculation(immatriculation)
                .marque(marque)
                .modele(modele)
                .capacitePoids(capacitePoids)
                .capaciteVolume(capaciteVolume)
                .disponible(true)
                .build());
    }

    private Client creerClient(String username, String password, String email,
                               String nom, String prenom, String telephone, String adresse) {
        if (clientRepository.existsByEmail(email)) {
            return clientRepository.findByEmail(email).orElseThrow();
        }
        Utilisateur utilisateur = creerUtilisateur(username, password, email, Role.CLIENT);
        return clientRepository.save(Client.builder()
                .nom(nom)
                .prenom(prenom)
                .telephone(telephone)
                .email(email)
                .adresse(adresse)
                .utilisateur(utilisateur)
                .build());
    }

    private Livreur creerLivreur(String username, String password, String email,
                                 String nom, String prenom, String telephone,
                                 Vehicule vehicule, Zone zone, StatutLivreur statut) {
        if (livreurRepository.existsByEmail(email)) {
            return livreurRepository.findByEmail(email).orElseThrow();
        }
        Utilisateur utilisateur = creerUtilisateur(username, password, email, Role.LIVREUR);
        return livreurRepository.save(Livreur.builder()
                .nom(nom)
                .prenom(prenom)
                .telephone(telephone)
                .email(email)
                .statut(statut)
                .utilisateur(utilisateur)
                .vehicule(vehicule)
                .zone(zone)
                .build());
    }

    private Commande creerCommande(String numero, Client client, Zone zone, Livreur livreur,
                                   Double distance, Double poids, Double volume,
                                   Priorite priorite, StatutCommande statut, int joursPrevue) {
        if (commandeRepository.existsByNumero(numero)) {
            return commandeRepository.findByNumero(numero).orElseThrow();
        }
        BigDecimal prix = prixService.calculerMontant(distance, poids, volume, priorite,
                zone != null ? zone.getId() : null);
        return commandeRepository.save(Commande.builder()
                .numero(numero)
                .adresseEnlevement("Entrepot central, " + (zone != null ? zone.getNom() : "inconnue"))
                .adresseLivraison("Residence destinaire, " + client.getAdresse())
                .poids(poids)
                .volume(volume)
                .priorite(priorite)
                .distance(distance)
                .prix(prix)
                .datePrevue(LocalDateTime.now().plusDays(joursPrevue))
                .statut(statut)
                .client(client)
                .livreur(livreur)
                .zone(zone)
                .build());
    }

    private Livraison creerLivraison(Commande commande, Livreur livreur, StatutLivraison statut) {
        if (livraisonRepository.findByCommandeId(commande.getId()).isPresent()) {
            return livraisonRepository.findByCommandeId(commande.getId()).orElseThrow();
        }
        Livraison livraison = Livraison.builder()
                .statut(statut)
                .commande(commande)
                .livreur(livreur)
                .dateLivraisonPrevue(commande.getDatePrevue())
                .build();
        if (statut == StatutLivraison.LIVREE) {
            livraison.setDateLivraisonReelle(LocalDateTime.now());
        }
        return livraisonRepository.save(livraison);
    }

    private void creerSuivi(Livraison livraison, StatutLivraison statut, String commentaire) {
        livraison.getSuivis().add(Suivi.builder()
                .statut(statut)
                .commentaire(commentaire)
                .livraison(livraison)
                .build());
    }

    private void creerPreuveLivraison(Commande commande) {
        if (preuveLivraisonRepository.existsByCommandeId(commande.getId())) {
            return;
        }
        preuveLivraisonRepository.save(PreuveLivraison.builder()
                .nomSignataire("Awa Diallo")
                .codeConfirmation("CONF-" + commande.getNumero())
                .dateLivraison(LocalDateTime.now())
                .commande(commande)
                .build());
    }

    private void creerPaiement(Commande commande) {
        String transactionId = "TXN-DEMO-" + commande.getNumero();
        if (paiementRepository.existsByTransactionId(transactionId)) {
            return;
        }
        paiementRepository.save(Paiement.builder()
                .montant(commande.getPrix())
                .methode(MethodePaiement.PAIEMENT_MOBILE)
                .statut(StatutPaiement.PAYE)
                .datePaiement(LocalDateTime.now())
                .transactionId(transactionId)
                .commande(commande)
                .build());
    }

    private Utilisateur creerUtilisateur(String username, String password, String email, Role role) {
        if (utilisateurRepository.existsByUsername(username)) {
            return utilisateurRepository.findByUsername(username).orElseThrow();
        }
        return utilisateurRepository.save(Utilisateur.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .role(role)
                .actif(true)
                .build());
    }
}