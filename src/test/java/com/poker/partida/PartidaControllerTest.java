package com.poker.partida;

import com.poker.ApiTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PartidaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ApiTestHelper helper;

    @BeforeEach
    void preparar() {
        helper = new ApiTestHelper(mockMvc);
    }

    @Test
    void criarPartida_retorna201ComId() throws Exception {
        Long criador = helper.cadastrarJogador();

        mockMvc.perform(post("/poker/partidas").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idJogador\":" + criador + ",\"nome\":\"Mesa VIP\",\"maxJogadores\":4}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void criarPartida_comJogadorInexistente_retorna404() throws Exception {
        mockMvc.perform(post("/poker/partidas").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idJogador\":999999,\"nome\":\"Mesa\",\"maxJogadores\":4}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void criarPartida_comMaxJogadoresInvalido_retorna400() throws Exception {
        Long criador = helper.cadastrarJogador();

        mockMvc.perform(post("/poker/partidas").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idJogador\":" + criador + ",\"nome\":\"Mesa\",\"maxJogadores\":11}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void partidasAbertas_listaMesaRecemCriadaComContagens() throws Exception {
        Long criador = helper.cadastrarJogador();
        Long idPartida = helper.criarPartida(criador, 4);

        mockMvc.perform(get("/poker/partidasAbertas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + idPartida + ")].jogadoresPresentes", contains(1)))
                .andExpect(jsonPath("$[?(@.id == " + idPartida + ")].maxJogadores", contains(4)));
    }
}
