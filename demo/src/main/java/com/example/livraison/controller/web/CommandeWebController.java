package com.example.livraison.controller.web;

import com.example.livraison.dto.request.ColisRequest;
import com.example.livraison.dto.request.CommandeRequest;
import com.example.livraison.dto.response.CommandeResponse;
import com.example.livraison.entity.StatutCommande;
import com.example.livraison.service.AdresseService;
import com.example.livraison.service.ClientService;
import com.example.livraison.service.CommandeService;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    private final AdresseService adresseService;

    public CommandeWebController(CommandeService commandeService, ClientService clientService,
                                 AdresseService adresseService) {
        this.commandeService = commandeService;
        this.clientService = clientService;
        this.adresseService = adresseService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("commandes", commandeService.getAll());
        model.addAttribute("statuts", StatutCommande.values());
        model.addAttribute("active", "commandes");
        return "commandes/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        CommandeRequest request = CommandeRequest.builder()
                .statut(StatutCommande.EN_ATTENTE)
                .colis(new ArrayList<>(List.of(new ColisRequest())))
                .build();
        model.addAttribute("commandeRequest", request);
        populateForm(model);
        model.addAttribute("editMode", false);
        model.addAttribute("active", "commandes");
        return "commandes/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("commandeRequest") CommandeRequest request,
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
    public String editForm(@PathVariable Long id, Model model) {
        CommandeResponse commande = commandeService.getById(id);
        CommandeRequest request = CommandeRequest.builder()
                .clientId(commande.clientId())
                .adresseId(commande.adresseLivraison() != null ? commande.adresseLivraison().id() : null)
                .statut(StatutCommande.valueOf(commande.statut()))
                .montantTotal(commande.montantTotal())
                .colis(commande.colis().stream()
                        .map(c -> ColisRequest.builder()
                                .description(c.description())
                                .poids(c.poids())
                                .longueur(c.longueur())
                                .largeur(c.largeur())
                                .hauteur(c.hauteur())
                                .fragile(c.fragile())
                                .build())
                        .toList())
                .build();
        model.addAttribute("commandeRequest", request);
        model.addAttribute("commandeId", id);
        populateForm(model);
        model.addAttribute("editMode", true);
        model.addAttribute("active", "commandes");
        return "commandes/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("commandeRequest") CommandeRequest request,
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
        return "redirect:/commandes";
    }

    @PostMapping("/{id}/statut")
    public String updateStatut(@PathVariable Long id, @RequestParam StatutCommande statut,
                               RedirectAttributes redirectAttributes) {
        commandeService.updateStatut(id, statut);
        redirectAttributes.addFlashAttribute("successMessage", "Statut de la commande mis a jour.");
        return "redirect:/commandes/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        commandeService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Commande supprimee.");
        return "redirect:/commandes";
    }

    private void populateForm(Model model) {
        model.addAttribute("clients", clientService.getAll());
        model.addAttribute("adresses", adresseService.getAll());
        model.addAttribute("statuts", StatutCommande.values());
    }
}
