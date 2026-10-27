package com.poker.carta;

public record Carta(Valor valor, Naipe naipe) {

    /** Ex.: "A♠", "10♥". */
    public String descricao() {
        return valor.getSimbolo() + naipe.getSimbolo();
    }

    @Override
    public String toString() {
        return descricao();
    }
}
