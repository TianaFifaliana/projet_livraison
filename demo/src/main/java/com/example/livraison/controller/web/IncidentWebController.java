package com.example.livraison.controller.web;

import com.example.livraison.dto.request.CreateIncidentRequest;
import com.example.livraison.dto.request.ResoudreIncidentRequest;
import com.example.livraison.entity.TypeIncident;
import com.example.livraison.service.CommandeService;
import com.example.livraison.service.IncidentService;
import com.example.livraison.service.LivreurService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/incidents")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER','OPERATOR')")
public class IncidentWebController {

    private final IncidentService incidentService;
    private final CommandeService commandeService;
    private final LivreurService livreurService;

    public IncidentWebController(IncidentService incidentService, CommandeService commandeService,
                                 LivreurService livreurService) {
        this.incidentService = incidentService;
        this.commandeService = commandeService;
        this.livreurService = livreurService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("incidents", incidentService.getAll());
        model.addAttribute("types", TypeIncident.values());
        model.addAttribute("commandes", commandeService.getAll());
        model.addAttribute("livreurs", livreurService.getAll());
        model.addAttribute("incidentRequest", new CreateIncidentRequest());
        model.addAttribute("resoudreRequest", new ResoudreIncidentRequest());
        model.addAttribute("active", "incidents");
        return "incidents/list";
    }

    @PostMapping
    public String signaler(@Valid @ModelAttribute("incidentRequest") CreateIncidentRequest request,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors du signalement de l'incident.");
            return "redirect:/incidents";
        }
        incidentService.signaler(request);
        redirectAttributes.addFlashAttribute("successMessage", "Incident signale avec succes.");
        return "redirect:/incidents";
    }

    @PostMapping("/{id}/resoudre")
    public String resoudre(@PathVariable Long id,
                           @Valid @ModelAttribute("resoudreRequest") ResoudreIncidentRequest request,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Impossible de resoudre l'incident.");
            return "redirect:/incidents";
        }
        incidentService.resoudre(id, request.getCommentaireResolution());
        redirectAttributes.addFlashAttribute("successMessage", "Incident resolu.");
        return "redirect:/incidents";
    }
}
