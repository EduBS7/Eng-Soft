package com.poker.partida;

public record EntrarPartidaResponse(
        boolean entrou,
        String mensagem,
        Long idPartida,
        Long idJogador,
        int jogadoresPresentes,
        int maxJogadores) {
}
