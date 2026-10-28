package com.poker.partida;

public record IniciarPartidaResponse(
        boolean iniciada,
        String mensagem,
        Long idPartida,
        int rodada,
        int jogadoresNaMesa) {
}
