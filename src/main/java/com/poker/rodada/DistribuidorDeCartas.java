package com.poker.rodada;

import com.poker.carta.Baralho;
import com.poker.carta.Carta;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * US13 – Distribui as cartas iniciais de uma rodada.
 * Regra do Texas Hold'em: 2 cartas fechadas por jogador, dadas uma de cada vez em duas voltas.
 */
@Component
public class DistribuidorDeCartas {

    public static final int CARTAS_POR_JOGADOR = 2;

    public Rodada iniciarRodada(int numeroRodada, List<Long> idsJogadores, Baralho baralho) {
        if (idsJogadores == null || idsJogadores.isEmpty()) {
            throw new IllegalArgumentException("É necessário ao menos um jogador para distribuir cartas.");
        }
        if (new HashSet<>(idsJogadores).size() != idsJogadores.size()) {
            throw new IllegalArgumentException("A lista de jogadores contém ids repetidos.");
        }

        Map<Long, List<Carta>> maos = new LinkedHashMap<>();
        for (Long id : idsJogadores) {
            maos.put(id, new ArrayList<>(CARTAS_POR_JOGADOR));
        }

        for (int volta = 0; volta < CARTAS_POR_JOGADOR; volta++) {
            for (Long id : idsJogadores) {
                maos.get(id).add(baralho.comprar());
            }
        }
        return new Rodada(numeroRodada, baralho, maos);
    }
}
