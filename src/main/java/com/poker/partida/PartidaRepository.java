package com.poker.partida;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PartidaRepository {

    // SkipListMap mantém as partidas ordenadas por id
    private final Map<Long, Partida> partidas = new ConcurrentSkipListMap<>();
    private final AtomicLong sequencia = new AtomicLong();

    public Partida criar(String nome, Long idCriador, int maxJogadores) {
        Partida partida = new Partida(sequencia.incrementAndGet(), nome, idCriador, maxJogadores);
        partidas.put(partida.getId(), partida);
        return partida;
    }

    public Optional<Partida> buscarPorId(Long id) {
        return Optional.ofNullable(partidas.get(id));
    }

    public List<Partida> listarTodas() {
        return List.copyOf(partidas.values());
    }
}
