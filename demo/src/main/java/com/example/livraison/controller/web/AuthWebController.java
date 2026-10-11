package com.example.livraison.controller.web;

import com.example.livraison.dto.request.RegisterRequest;
import com.example.livraison.entity.Role;
import com.example.livraison.exception.BusinessException;
import com.example.livraison.exception.DuplicateResourceException;
import com.example.livraison.service.AuthService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthWebController {

    private final AuthService authService;

    public AuthWebController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        if (!model.containsAttribute("registerRequest")) {
            model.addAttribute("registerRequest", new RegisterRequest());
        }
        model.addAttribute("roles", List.of(Role.CLIENT, Role.LIVREUR));
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerRequest") RegisterRequest request,
                           BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", List.of(Role.CLIENT, Role.LIVREUR));
            return "register";
        }
        try {
            authService.register(request);
        } catch (DuplicateResourceException | BusinessException ex) {
            model.addAttribute("roles", List.of(Role.CLIENT, Role.LIVREUR));
            model.addAttribute("errorMessage", ex.getMessage());
            return "register";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Compte cree avec succes, vous pouvez vous connecter.");
        return "redirect:/login";
    }
}
