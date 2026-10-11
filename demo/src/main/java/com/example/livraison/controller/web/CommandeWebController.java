package com.example.livraison.controller.web;

import com.example.livraison.dto.request.CreateCommandeRequest;
import com.example.livraison.dto.request.UpdateCommandeRequest;
import com.example.livraison.dto.request.ValidationGroups;
import com.example.livraison.dto.response.CommandeResponse;
import com.example.livraison.entity.Priorite;
import com.example.livraison.entity.StatutCommande;
import com.example.livraison.service.ClientService;
import com.example.livraison.service.CommandeService;
import com.example.livraison.service.LivreurService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/commandes")
public class CommandeWebController {

    private final CommandeService commandeService;
    private final ClientService clientService;
    private final LivreurService livreurService;

    public CommandeWebController(CommandeService commandeService, ClientService clientService,
                                 LivreurService livreurService) {
        this.commandeService = commandeService;
        this.clientService = clientService;
        this.livreurService = livreurService;
    }

    @GetMapping
    public String list(
            @RequestParam(required = false) StatutCommande statut,
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long livreurId,
            @RequestParam(required = false) Priorite priorite,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        int size = 10;
        Page<CommandeResponse> result = commandeService.rechercher(
                statut, clientId, livreurId, priorite, dateDebut, dateFin,
                PageRequest.of(Math.max(page, 0), size, Sort.by(Sort.Direction.DESC, "dateCreation")));
        model.addAttribute("commandes", result);
        model.addAttribute("page", result);
        model.addAttribute("statut", statut);
        model.addAttribute("clientId", clientId);
        model.addAttribute("livreurId", livreurId);
        model.addAttribute("priorite", priorite);
        model.addAttribute("dateDebut", dateDebut);
        model.addAttribute("dateFin", dateFin);
        model.addAttribute("statuts", StatutCommande.values());
        model.addAttribute("priorites", Priorite.values());
        model.addAttribute("clients", clientService.getAll());
        model.addAttribute("livreurs", livreurService.getAll());
        model.addAttribute("active", "commandes");
        return "commandes/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','OPERATOR')")
    public String createForm(Model model) {
        model.addAttribute("commandeRequest", CreateCommandeRequest.builder()
                .statut(StatutCommande.CREEE)
                .priorite(Priorite.NORMALE)
                .build());
        populateForm(model);
        model.addAttribute("editMode", false);
        model.addAttribute("active", "commandes");
        return "commandes/form";
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','OPERATOR')")
    public String create(@Validated(ValidationGroups.Web.class) @ModelAttribute("commandeRequest")
                         CreateCommandeRequest request,
                         BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            populateForm(model);
            model.addAttribute("editMode", false);
            model.addAttribute("active", "commandes");
            return "commandes/form";
        }
        commandeService.create(request);
        redirectAttributes.addFlashAttribute("successMessage", "Commande creee avec succes.");
        return "redirect:/commandes";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("commande", commandeService.getById(id));
        model.addAttribute("statuts", StatutCommande.values());
        model.addAttribute("active", "commandes");
        return "commandes/detail";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','OPERATOR')")
    public String editForm(@PathVariable Long id, Model model) {
        CommandeResponse commande = commandeService.getById(id);
        UpdateCommandeRequest request = UpdateCommandeRequest.builder()
                .clientId(commande.clientId())
                .adresseEnlevement(commande.adresseEnlevement())
                .adresseLivraison(commande.adresseLivraison())
                .poids(commande.poids())
                .volume(commande.volume())
                .priorite(Priorite.valueOf(commande.priorite()))
                .distance(commande.distance())
                .prix(commande.prix())
                .datePrevue(commande.datePrevue())
                .statut(StatutCommande.valueOf(commande.statut()))
                .livreurId(commande.livreurId())
                .zoneId(commande.zoneId())
                .build();
        model.addAttribute("commandeRequest", request);
        model.addAttribute("commandeId", id);
        model.addAttribute("clientNom", commande.clientNom());
        populateForm(model);
        model.addAttribute("editMode", true);
        model.addAttribute("active", "commandes");
        return "commandes/form";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','OPERATOR')")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("commandeRequest") UpdateCommandeRequest request,
                         BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("commandeId", id);
            populateForm(model);
            model.addAttribute("editMode", true);
            model.addAttribute("active", "commandes");
            return "commandes/form";
        }
        commandeService.update(id, request);
        redirectAttributes.addFlashAttribute("successMessage", "Commande mise a jour avec succes.");
        return "redirect:/commandes/" + id;
    }

    @PostMapping("/{id}/statut")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','OPERATOR')")
    public String changerStatut(@PathVariable Long id, @RequestParam StatutCommande statut,
                                RedirectAttributes redirectAttributes) {
        commandeService.changerStatut(id, statut);
        redirectAttributes.addFlashAttribute("successMessage", "Statut de la commande mis a jour.");
        return "redirect:/commandes/" + id;
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','OPERATOR')")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        commandeService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Commande supprimee.");
        return "redirect:/commandes";
    }

    private void populateForm(Model model) {
        model.addAttribute("livreurs", livreurService.getAll());
        model.addAttribute("statuts", StatutCommande.values());
        model.addAttribute("priorites", Priorite.values());
    }
}