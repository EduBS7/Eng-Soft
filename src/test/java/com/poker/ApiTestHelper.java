package com.poker;

import com.jayway.jsonpath.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Atalhos para montar cenários nos testes de integração. */
public class ApiTestHelper {

    private final MockMvc mockMvc;

    public ApiTestHelper(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    /** Cadastra um jogador com e-mail único e devolve o id. */
    public Long cadastrarJogador() throws Exception {
        String corpo = "{\"nome\":\"Jogador Teste\",\"email\":\"jogador-" + UUID.randomUUID()
                + "@teste.com\",\"senha\":\"senha123\"}";
        MvcResult resultado = mockMvc.perform(post("/poker/cadastro")
                        .contentType(MediaType.APPLICATION_JSON).content(corpo))
                .andExpect(status().isCreated())
                .andReturn();
        return lerId(resultado);
    }

    public Long criarPartida(Long idCriador, int maxJogadores) throws Exception {
        String corpo = "{\"idJogador\":" + idCriador + ",\"nome\":\"Mesa de teste\",\"maxJogadores\":"
                + maxJogadores + "}";
        MvcResult resultado = mockMvc.perform(post("/poker/partidas")
                        .contentType(MediaType.APPLICATION_JSON).content(corpo))
                .andExpect(status().isCreated())
                .andReturn();
        return lerId(resultado);
    }

    public void entrar(Long idPartida, Long idJogador) throws Exception {
        mockMvc.perform(post("/poker/" + idPartida + "/entrar").param("idJogador", idJogador.toString()))
                .andExpect(status().isOk());
    }

    public void iniciar(Long idPartida, Long idCriador) throws Exception {
        mockMvc.perform(post("/poker/" + idPartida + "/iniciar").param("idJogador", idCriador.toString()))
                .andExpect(status().isOk());
    }

    public static Long lerId(MvcResult resultado) throws Exception {
        Number id = JsonPath.read(corpo(resultado), "$.id");
        return id.longValue();
    }

    public static String corpo(MvcResult resultado) throws Exception {
        return resultado.getResponse().getContentAsString(StandardCharsets.UTF_8);
    }
}
