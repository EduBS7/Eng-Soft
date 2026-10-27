package com.poker.rodada;

import com.poker.carta.Baralho;
import com.poker.carta.Carta;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Uma rodada (mão) de pôquer: guarda o baralho usado e as cartas de cada jogador.
 * O baralho fica aqui porque as próximas histórias (flop, turn, river) vão comprar dele.
 */
public class Rodada {

    private final int numero;
    private final Baralho baralho;
    private final Map<Long, List<Carta>> maos;

    Rodada(int numero, Baralho baralho, Map<Long, List<Carta>> maos) {
        this.numero = numero;
        this.baralho = baralho;
        this.maos = Collections.unmodifiableMap(maos);
    }

    public int getNumero() {
        return numero;
    }

    /** Cartas do jogador (cópia imutável). Lista vazia se o jogador não participa da rodada. */
    public List<Carta> getMao(Long idJogador) {
        List<Carta> mao = maos.get(idJogador);
        return mao == null ? List.of() : List.copyOf(mao);
    }

    public int getCartasRestantesNoBaralho() {
        return baralho.restantes();
    }
}
