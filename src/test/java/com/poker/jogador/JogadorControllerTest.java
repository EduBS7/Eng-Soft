package com.poker.jogador;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JogadorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String json(String nome, String email, String senha) {
        return "{\"nome\":\"" + nome + "\",\"email\":\"" + email + "\",\"senha\":\"" + senha + "\"}";
    }

    @Test
    void cadastroValido_retorna201ComId() throws Exception {
        String email = "ana-" + UUID.randomUUID() + "@teste.com";

        mockMvc.perform(post("/poker/cadastro").contentType(MediaType.APPLICATION_JSON)
                        .content(json("Ana", email, "senha123")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void emailDuplicado_retorna409() throws Exception {
        String email = "duplicado-" + UUID.randomUUID() + "@teste.com";
        String corpo = json("Bia", email, "senha123");

        mockMvc.perform(post("/poker/cadastro").contentType(MediaType.APPLICATION_JSON).content(corpo))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/poker/cadastro").contentType(MediaType.APPLICATION_JSON)
                        .content(json("Outra Bia", email.toUpperCase(), "outraSenha1")))
                .andExpect(status().isConflict());
    }

    @Test
    void dadosInvalidos_retorna400ComDetalhes() throws Exception {
        mockMvc.perform(post("/poker/cadastro").contentType(MediaType.APPLICATION_JSON)
                        .content(json("", "email-invalido", "123")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes").isArray());
    }

    @Test
    void jsonMalformado_retorna400() throws Exception {
        mockMvc.perform(post("/poker/cadastro").contentType(MediaType.APPLICATION_JSON).content("{nao e json"))
                .andExpect(status().isBadRequest());
    }
}
