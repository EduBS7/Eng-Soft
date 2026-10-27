package com.poker.carta;

import java.security.SecureRandom;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/**
 * Baralho inglês de 52 cartas, embaralhado na criação.
 * Não é thread-safe: quem usa deve garantir acesso exclusivo (a Partida faz isso).
 */
public class Baralho {

    public static final int TOTAL_CARTAS = 52;

    private final Deque<Carta> cartas;

    /** Usa SecureRandom, adequado para jogo valendo "dinheiro". */
    public Baralho() {
        this(new SecureRandom());
    }

    /** Permite informar o Random (útil em testes, com semente fixa). */
    public Baralho(Random random) {
        List<Carta> todas = new ArrayList<>(TOTAL_CARTAS);
        for (Naipe naipe : Naipe.values()) {
            for (Valor valor : Valor.values()) {
                todas.add(new Carta(valor, naipe));
            }
        }
        Collections.shuffle(todas, random);
        this.cartas = new ArrayDeque<>(todas);
    }

    public Carta comprar() {
        if (cartas.isEmpty()) {
            throw new IllegalStateException("O baralho está vazio.");
        }
        return cartas.pop();
    }

    public int restantes() {
        return cartas.size();
    }
}
