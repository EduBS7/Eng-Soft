package com.poker.rodada;

import com.poker.carta.Baralho;
import com.poker.carta.Carta;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistribuidorDeCartasTest {

    private final DistribuidorDeCartas distribuidor = new DistribuidorDeCartas();

    @Test
    void cadaJogadorRecebeDuasCartas() {
        Rodada rodada = distribuidor.iniciarRodada(1, List.of(1L, 2L, 3L), new Baralho());

        assertThat(rodada.getMao(1L)).hasSize(2);
        assertThat(rodada.getMao(2L)).hasSize(2);
        assertThat(rodada.getMao(3L)).hasSize(2);
    }

    @Test
    void nenhumaCartaSeRepeteEntreOsJogadores() {
        List<Long> jogadores = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);

        Rodada rodada = distribuidor.iniciarRodada(1, jogadores, new Baralho());

        Set<Carta> todas = new HashSet<>();
        for (Long id : jogadores) {
            todas.addAll(rodada.getMao(id));
        }
        assertThat(todas).hasSize(20);
        assertThat(rodada.getCartasRestantesNoBaralho()).isEqualTo(32);
    }

    @Test
    void distribuicaoEhEmVoltas_umaCartaPorVez() {
        // Com a mesma semente, o baralho sai na mesma ordem: c0..c5.
        Baralho referencia = new Baralho(new Random(7));
        Carta c0 = referencia.comprar(), c1 = referencia.comprar(), c2 = referencia.comprar(),
                c3 = referencia.comprar(), c4 = referencia.comprar(), c5 = referencia.comprar();

        Rodada rodada = distribuidor.iniciarRodada(1, List.of(10L, 20L, 30L), new Baralho(new Random(7)));

        // 1ª volta: c0, c1, c2 | 2ª volta: c3, c4, c5
        assertThat(rodada.getMao(10L)).containsExactly(c0, c3);
        assertThat(rodada.getMao(20L)).containsExactly(c1, c4);
        assertThat(rodada.getMao(30L)).containsExactly(c2, c5);
    }

    @Test
    void jogadorForaDaRodada_temMaoVazia() {
        Rodada rodada = distribuidor.iniciarRodada(1, List.of(1L, 2L), new Baralho());

        assertThat(rodada.getMao(99L)).isEmpty();
    }

    @Test
    void guardaONumeroDaRodada() {
        Rodada rodada = distribuidor.iniciarRodada(3, List.of(1L, 2L), new Baralho());

        assertThat(rodada.getNumero()).isEqualTo(3);
    }

    @Test
    void semJogadores_lancaExcecao() {
        assertThatThrownBy(() -> distribuidor.iniciarRodada(1, List.of(), new Baralho()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void jogadoresRepetidos_lancaExcecao() {
        assertThatThrownBy(() -> distribuidor.iniciarRodada(1, List.of(1L, 1L), new Baralho()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
