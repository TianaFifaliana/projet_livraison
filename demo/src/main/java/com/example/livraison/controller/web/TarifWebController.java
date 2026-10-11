package com.example.livraison.controller.web;

import com.example.livraison.dto.request.TarifRequest;
import com.example.livraison.dto.response.TarifResponse;
import com.example.livraison.service.TarifService;
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
@RequestMapping("/tarifs")
@PreAuthorize("hasRole('ADMIN')")
public class TarifWebController {

    private final TarifService tarifService;

    public TarifWebController(TarifService tarifService) {
        this.tarifService = tarifService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("tarifs", tarifService.getAll());
        model.addAttribute("active", "tarifs");
        return "tarifs/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("tarifRequest", TarifRequest.builder().parDefaut(false).build());
        model.addAttribute("editMode", false);
        model.addAttribute("active", "tarifs");
        return "tarifs/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("tarifRequest") TarifRequest request,
                         BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", false);
            model.addAttribute("active", "tarifs");
            return "tarifs/form";
        }
        tarifService.create(request);
        redirectAttributes.addFlashAttribute("successMessage", "Tarif cree avec succes.");
        return "redirect:/tarifs";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        TarifResponse tarif = tarifService.getById(id);
        TarifRequest request = TarifRequest.builder()
                .libelle(tarif.libelle())
                .prixBase(tarif.prixBase())
                .prixParKm(tarif.prixParKm())
                .prixParKg(tarif.prixParKg())
                .prixParM3(tarif.prixParM3())
                .coefficientZone(tarif.coefficientZone())
                .parDefaut(tarif.parDefaut())
                .poidsMax(tarif.poidsMax())
                .zoneId(tarif.zoneId())
                .build();
        model.addAttribute("tarifRequest", request);
        model.addAttribute("tarifId", id);
        model.addAttribute("editMode", true);
        model.addAttribute("active", "tarifs");
        return "tarifs/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("tarifRequest") TarifRequest request,
                         BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("tarifId", id);
            model.addAttribute("editMode", true);
            model.addAttribute("active", "tarifs");
            return "tarifs/form";
        }
        tarifService.update(id, request);
        redirectAttributes.addFlashAttribute("successMessage", "Tarif mis a jour avec succes.");
        return "redirect:/tarifs";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        tarifService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Tarif supprime.");
        return "redirect:/tarifs";
    }
}
