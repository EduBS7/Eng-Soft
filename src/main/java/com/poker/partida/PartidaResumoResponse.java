package com.poker.partida;

public record PartidaResumoResponse(Long id, String nome, int jogadoresPresentes, int maxJogadores) {

    public static PartidaResumoResponse de(Partida partida) {
        return new PartidaResumoResponse(partida.getId(), partida.getNome(),
                partida.getQuantidadeJogadores(), partida.getMaxJogadores());
    }
}
