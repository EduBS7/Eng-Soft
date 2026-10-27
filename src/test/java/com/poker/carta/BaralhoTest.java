package com.poker.carta;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BaralhoTest {

    @Test
    void baralhoNovoTem52CartasDistintas() {
        Baralho baralho = new Baralho();
        Set<Carta> cartas = new HashSet<>();

        while (baralho.restantes() > 0) {
            cartas.add(baralho.comprar());
        }

        assertThat(cartas).hasSize(Baralho.TOTAL_CARTAS);
    }

    @Test
    void comprarReduzAQuantidadeRestante() {
        Baralho baralho = new Baralho();

        baralho.comprar();

        assertThat(baralho.restantes()).isEqualTo(51);
    }

    @Test
    void comprarDeBaralhoVazio_lancaExcecao() {
        Baralho baralho = new Baralho();
        for (int i = 0; i < Baralho.TOTAL_CARTAS; i++) {
            baralho.comprar();
        }

        assertThatThrownBy(baralho::comprar).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void mesmaSemente_geraMesmaOrdem() {
        List<Carta> a = sequencia(new Baralho(new Random(42)));
        List<Carta> b = sequencia(new Baralho(new Random(42)));

        assertThat(a).isEqualTo(b);
    }

    @Test
    void sementesDiferentes_geramOrdensDiferentes() {
        List<Carta> a = sequencia(new Baralho(new Random(1)));
        List<Carta> b = sequencia(new Baralho(new Random(2)));

        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void descricaoDaCartaUsaValorENaipe() {
        assertThat(new Carta(Valor.AS, Naipe.ESPADAS).descricao()).isEqualTo("A\u2660");
        assertThat(new Carta(Valor.DEZ, Naipe.COPAS).descricao()).isEqualTo("10\u2665");
    }

    private List<Carta> sequencia(Baralho baralho) {
        List<Carta> cartas = new ArrayList<>();
        while (baralho.restantes() > 0) {
            cartas.add(baralho.comprar());
        }
        return cartas;
    }
}
