package com.poker.partida;

import com.poker.ApiTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ParticipacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ApiTestHelper helper;

    @BeforeEach
    void preparar() {
        helper = new ApiTestHelper(mockMvc);
    }

    // ---------- US10 – entrar ----------

    @Test
    void entrar_comSucesso() throws Exception {
        Long criador = helper.cadastrarJogador();
        Long visitante = helper.cadastrarJogador();
        Long idPartida = helper.criarPartida(criador, 4);

        mockMvc.perform(post("/poker/" + idPartida + "/entrar").param("idJogador", visitante.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entrou").value(true))
                .andExpect(jsonPath("$.jogadoresPresentes").value(2))
                .andExpect(jsonPath("$.maxJogadores").value(4));
    }

    @Test
    void entrar_mesaCheia_retorna409() throws Exception {
        Long criador = helper.cadastrarJogador();
        Long segundo = helper.cadastrarJogador();
        Long terceiro = helper.cadastrarJogador();
        Long idPartida = helper.criarPartida(criador, 2);
        helper.entrar(idPartida, segundo);

        mockMvc.perform(post("/poker/" + idPartida + "/entrar").param("idJogador", terceiro.toString()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensagem", containsString("cheia")));
    }

    @Test
    void entrar_partidaJaIniciada_retorna409() throws Exception {
        Long criador = helper.cadastrarJogador();
        Long segundo = helper.cadastrarJogador();
        Long retardatario = helper.cadastrarJogador();
        Long idPartida = helper.criarPartida(criador, 4);
        helper.entrar(idPartida, segundo);
        helper.iniciar(idPartida, criador);

        mockMvc.perform(post("/poker/" + idPartida + "/entrar").param("idJogador", retardatario.toString()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensagem", containsString("iniciada")));
    }

    @Test
    void entrar_jogadorQueJaEstaNaMesa_retorna409() throws Exception {
        Long criador = helper.cadastrarJogador();
        Long idPartida = helper.criarPartida(criador, 4);

        mockMvc.perform(post("/poker/" + idPartida + "/entrar").param("idJogador", criador.toString()))
                .andExpect(status().isConflict());
    }

    @Test
    void entrar_partidaInexistente_retorna404() throws Exception {
        Long jogador = helper.cadastrarJogador();

        mockMvc.perform(post("/poker/999999/entrar").param("idJogador", jogador.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void entrar_jogadorInexistente_retorna404() throws Exception {
        Long criador = helper.cadastrarJogador();
        Long idPartida = helper.criarPartida(criador, 4);

        mockMvc.perform(post("/poker/" + idPartida + "/entrar").param("idJogador", "999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void entrar_semIdJogador_retorna400() throws Exception {
        Long criador = helper.cadastrarJogador();
        Long idPartida = helper.criarPartida(criador, 4);

        mockMvc.perform(post("/poker/" + idPartida + "/entrar"))
                .andExpect(status().isBadRequest());
    }

    // ---------- US12 – iniciar ----------

    @Test
    void iniciar_comSucesso() throws Exception {
        Long criador = helper.cadastrarJogador();
        Long segundo = helper.cadastrarJogador();
        Long idPartida = helper.criarPartida(criador, 4);
        helper.entrar(idPartida, segundo);

        mockMvc.perform(post("/poker/" + idPartida + "/iniciar").param("idJogador", criador.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iniciada").value(true))
                .andExpect(jsonPath("$.rodada").value(1))
                .andExpect(jsonPath("$.jogadoresNaMesa").value(2));
    }

    @Test
    void iniciar_semJogadoresSuficientes_retorna409() throws Exception {
        Long criador = helper.cadastrarJogador();
        Long idPartida = helper.criarPartida(criador, 4);

        mockMvc.perform(post("/poker/" + idPartida + "/iniciar").param("idJogador", criador.toString()))
                .andExpect(status().isConflict());
    }

    @Test
    void iniciar_porQuemNaoECriador_retorna403() throws Exception {
        Long criador = helper.cadastrarJogador();
        Long segundo = helper.cadastrarJogador();
        Long idPartida = helper.criarPartida(criador, 4);
        helper.entrar(idPartida, segundo);

        mockMvc.perform(post("/poker/" + idPartida + "/iniciar").param("idJogador", segundo.toString()))
                .andExpect(status().isForbidden());
    }

    @Test
    void iniciar_partidaInexistente_retorna404() throws Exception {
        mockMvc.perform(post("/poker/999999/iniciar").param("idJogador", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void iniciar_duasVezes_retorna409() throws Exception {
        Long criador = helper.cadastrarJogador();
        Long segundo = helper.cadastrarJogador();
        Long idPartida = helper.criarPartida(criador, 4);
        helper.entrar(idPartida, segundo);
        helper.iniciar(idPartida, criador);

        mockMvc.perform(post("/poker/" + idPartida + "/iniciar").param("idJogador", criador.toString()))
                .andExpect(status().isConflict());
    }

    @Test
    void partidaIniciada_naoApareceMaisEmPartidasAbertas() throws Exception {
        Long criador = helper.cadastrarJogador();
        Long segundo = helper.cadastrarJogador();
        Long idPartida = helper.criarPartida(criador, 4);
        helper.entrar(idPartida, segundo);
        helper.iniciar(idPartida, criador);

        mockMvc.perform(get("/poker/partidasAbertas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + idPartida + ")]", empty()));
    }

    @Test
    void mesaCheia_naoApareceEmPartidasAbertas() throws Exception {
        Long criador = helper.cadastrarJogador();
        Long segundo = helper.cadastrarJogador();
        Long idPartida = helper.criarPartida(criador, 2);
        helper.entrar(idPartida, segundo);

        mockMvc.perform(get("/poker/partidasAbertas"))
                .andExpect(jsonPath("$[?(@.id == " + idPartida + ")]", empty()));
    }
}
