package com.example.livraison.dto.request;

import com.example.livraison.entity.RoleName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Size(min = 3, max = 50, message = "Le nom d'utilisateur doit contenir entre 3 et 50 caracteres")
    private String username;

    @Size(min = 6, max = 100, message = "Le mot de passe doit contenir au moins 6 caracteres")
    private String password;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit etre valide")
    private String email;

    @Builder.Default
    private boolean enabled = true;

    @NotEmpty(message = "Au moins un role est obligatoire")
    private Set<RoleName> roles;
}
