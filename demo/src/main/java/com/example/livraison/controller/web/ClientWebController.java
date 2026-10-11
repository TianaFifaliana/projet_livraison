package com.example.livraison.controller.web;

import com.example.livraison.dto.request.CreateClientRequest;
import com.example.livraison.dto.response.ClientResponse;
import com.example.livraison.service.ClientService;
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
@RequestMapping("/clients")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
public class ClientWebController {

    private final ClientService clientService;
    private final UserService userService;

    public ClientWebController(ClientService clientService, UserService userService) {
        this.clientService = clientService;
        this.userService = userService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("clients", clientService.getAll());
        model.addAttribute("active", "clients");
        return "clients/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("clientRequest", new CreateClientRequest());
        model.addAttribute("users", userService.getAll());
        model.addAttribute("editMode", false);
        model.addAttribute("active", "clients");
        return "clients/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("clientRequest") CreateClientRequest request,
                         BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.getAll());
            model.addAttribute("editMode", false);
            model.addAttribute("active", "clients");
            return "clients/form";
        }
        clientService.create(request);
        redirectAttributes.addFlashAttribute("successMessage", "Client cree avec succes.");
        return "redirect:/clients";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        ClientResponse client = clientService.getById(id);
        CreateClientRequest request = CreateClientRequest.builder()
                .nom(client.nom())
                .prenom(client.prenom())
                .telephone(client.telephone())
                .email(client.email())
                .adresse(client.adresse())
                .utilisateurId(client.utilisateurId())
                .build();
        model.addAttribute("clientRequest", request);
        model.addAttribute("clientId", id);
        model.addAttribute("users", userService.getAll());
        model.addAttribute("editMode", true);
        model.addAttribute("active", "clients");
        return "clients/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("clientRequest") CreateClientRequest request,
                         BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("clientId", id);
            model.addAttribute("users", userService.getAll());
            model.addAttribute("editMode", true);
            model.addAttribute("active", "clients");
            return "clients/form";
        }
        clientService.update(id, request);
        redirectAttributes.addFlashAttribute("successMessage", "Client mis a jour avec succes.");
        return "redirect:/clients";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        clientService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Client supprime.");
        return "redirect:/clients";
    }
}