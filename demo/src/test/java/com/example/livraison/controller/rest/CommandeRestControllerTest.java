package com.example.livraison.controller.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.livraison.dto.response.CommandeResponse;
import com.example.livraison.exception.ResourceNotFoundException;
import com.example.livraison.security.JwtAuthenticationFilter;
import com.example.livraison.service.CommandeService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        controllers = CommandeRestController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = false)
class CommandeRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommandeService commandeService;

    private static final String CORPS_VALIDE = """
            {
              "clientId": 1,
              "adresseEnlevement": "Entrepot central",
              "adresseLivraison": "Residence destinataire",
              "poids": 10.0,
              "volume": 2.0,
              "priorite": "NORMALE",
              "distance": 12.5,
              "zoneId": 1
            }
            """;

    @Test
    @DisplayName("POST /api/orders avec un corps valide -> 201 Created")
    void creerCommandeValide() throws Exception {
        CommandeResponse response = new CommandeResponse(
                1L, "CMD-1001", "Entrepot central", "Residence destinataire",
                10.0, 2.0, "NORMALE", 12.5, new BigDecimal("19.50"),
                LocalDateTime.now().plusDays(1), LocalDateTime.now(), "CREEE",
                1L, "Awa Diallo", null, 1L);
        when(commandeService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CORPS_VALIDE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.numero").value("CMD-1001"))
                .andExpect(jsonPath("$.statut").value("CREEE"))
                .andExpect(jsonPath("$.prix").value(19.50));

        verify(commandeService).create(any());
    }

    @Test
    @DisplayName("POST /api/orders avec un corps invalide -> 400 Bad Request")
    void creerCommandeInvalide() throws Exception {
        String corpsInvalide = """
                {
                  "adresseEnlevement": "",
                  "volume": -1,
                  "priorite": "NORMALE"
                }
                """;

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpsInvalide))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.details.clientId").value("Le client est obligatoire"))
                .andExpect(jsonPath("$.details.adresseLivraison").exists());
    }

    @Test
    @DisplayName("GET /api/orders/{id} d'une commande inexistante -> 404 Not Found")
    void commandeInexistante() throws Exception {
        when(commandeService.getById(999L))
                .thenThrow(new ResourceNotFoundException("Commande", 999L));

        mockMvc.perform(get("/api/orders/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").exists());
    }
}