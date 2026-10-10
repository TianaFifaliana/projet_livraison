package com.example.livraison.controller.web;

import com.example.livraison.dto.request.LivraisonRequest;
import com.example.livraison.dto.request.SuiviRequest;
import com.example.livraison.entity.StatutLivraison;
import com.example.livraison.service.CommandeService;
import com.example.livraison.service.LivreurService;
import com.example.livraison.service.LivraisonService;
import com.example.livraison.service.SuiviService;
import jakarta.validation.Valid;
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
@RequestMapping("/livraisons")
public class LivraisonWebController {

    private final LivraisonService livraisonService;
    private final CommandeService commandeService;
    private final LivreurService livreurService;
    private final SuiviService suiviService;

    public LivraisonWebController(LivraisonService livraisonService, CommandeService commandeService,
                                  LivreurService livreurService, SuiviService suiviService) {
        this.livraisonService = livraisonService;
        this.commandeService = commandeService;
        this.livreurService = livreurService;
        this.suiviService = suiviService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("livraisons", livraisonService.getAll());
        model.addAttribute("statuts", StatutLivraison.values());
        model.addAttribute("active", "livraisons");
        return "livraisons/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("livraisonRequest", LivraisonRequest.builder().statut(StatutLivraison.PLANIFIEE).build());
        populateForm(model);
        model.addAttribute("active", "livraisons");
        return "livraisons/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("livraisonRequest") LivraisonRequest request,
                         BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            populateForm(model);
            model.addAttribute("active", "livraisons");
            return "livraisons/form";
        }
        livraisonService.create(request);
        redirectAttributes.addFlashAttribute("successMessage", "Livraison creee avec succes.");
        return "redirect:/livraisons";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("livraison", livraisonService.getById(id));
        model.addAttribute("suivis", suiviService.getByLivraison(id));
        model.addAttribute("livreurs", livreurService.getAll());
        model.addAttribute("statuts", StatutLivraison.values());
        model.addAttribute("suiviRequest", SuiviRequest.builder().livraisonId(id).build());
        model.addAttribute("active", "livraisons");
        return "livraisons/detail";
    }

    @PostMapping("/{id}/livreur")
    public String assignerLivreur(@PathVariable Long id, @RequestParam Long livreurId,
                                  RedirectAttributes redirectAttributes) {
        livraisonService.assignerLivreur(id, livreurId);
        redirectAttributes.addFlashAttribute("successMessage", "Livreur assigne a la livraison.");
        return "redirect:/livraisons/" + id;
    }

    @PostMapping("/{id}/statut")
    public String updateStatut(@PathVariable Long id, @RequestParam StatutLivraison statut,
                               RedirectAttributes redirectAttributes) {
        livraisonService.updateStatut(id, statut);
        redirectAttributes.addFlashAttribute("successMessage", "Statut de la livraison mis a jour.");
        return "redirect:/livraisons/" + id;
    }

    @PostMapping("/{id}/suivi")
    public String addSuivi(@PathVariable Long id, @Valid @ModelAttribute("suiviRequest") SuiviRequest request,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        request.setLivraisonId(id);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Le suivi n'a pas pu etre enregistre.");
            return "redirect:/livraisons/" + id;
        }
        suiviService.add(request);
        redirectAttributes.addFlashAttribute("successMessage", "Suivi ajoute.");
        return "redirect:/livraisons/" + id;
    }

    private void populateForm(Model model) {
        model.addAttribute("commandes", commandeService.getAll());
        model.addAttribute("livreurs", livreurService.getAll());
        model.addAttribute("statuts", StatutLivraison.values());
    }
}
