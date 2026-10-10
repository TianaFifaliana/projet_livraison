package com.example.livraison.controller.web;

import com.example.livraison.dto.request.LivreurRequest;
import com.example.livraison.dto.response.LivreurResponse;
import com.example.livraison.service.LivreurService;
import com.example.livraison.service.UserService;
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
@RequestMapping("/livreurs")
@PreAuthorize("hasRole('ADMIN')")
public class LivreurWebController {

    private final LivreurService livreurService;
    private final UserService userService;

    public LivreurWebController(LivreurService livreurService, UserService userService) {
        this.livreurService = livreurService;
        this.userService = userService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("livreurs", livreurService.getAll());
        model.addAttribute("active", "livreurs");
        return "livreurs/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("livreurRequest", LivreurRequest.builder().disponible(true).build());
        model.addAttribute("users", userService.getAll());
        model.addAttribute("editMode", false);
        model.addAttribute("active", "livreurs");
        return "livreurs/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("livreurRequest") LivreurRequest request,
                         BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.getAll());
            model.addAttribute("editMode", false);
            model.addAttribute("active", "livreurs");
            return "livreurs/form";
        }
        livreurService.create(request);
        redirectAttributes.addFlashAttribute("successMessage", "Livreur cree avec succes.");
        return "redirect:/livreurs";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        LivreurResponse livreur = livreurService.getById(id);
        LivreurRequest request = LivreurRequest.builder()
                .nom(livreur.nom())
                .prenom(livreur.prenom())
                .telephone(livreur.telephone())
                .email(livreur.email())
                .vehicule(livreur.vehicule())
                .disponible(livreur.disponible())
                .userId(livreur.userId())
                .build();
        model.addAttribute("livreurRequest", request);
        model.addAttribute("livreurId", id);
        model.addAttribute("users", userService.getAll());
        model.addAttribute("editMode", true);
        model.addAttribute("active", "livreurs");
        return "livreurs/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("livreurRequest") LivreurRequest request,
                         BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("livreurId", id);
            model.addAttribute("users", userService.getAll());
            model.addAttribute("editMode", true);
            model.addAttribute("active", "livreurs");
            return "livreurs/form";
        }
        livreurService.update(id, request);
        redirectAttributes.addFlashAttribute("successMessage", "Livreur mis a jour avec succes.");
        return "redirect:/livreurs";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        livreurService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Livreur supprime.");
        return "redirect:/livreurs";
    }
}
