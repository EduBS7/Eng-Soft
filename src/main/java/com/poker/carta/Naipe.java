package com.poker.carta;

public enum Naipe {
    PAUS("\u2663"),
    OUROS("\u2666"),
    COPAS("\u2665"),
    ESPADAS("\u2660");

    private final String simbolo;

    Naipe(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getSimbolo() {
        return simbolo;
    }
}
