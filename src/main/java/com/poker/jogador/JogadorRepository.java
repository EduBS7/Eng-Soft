package com.poker.jogador;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repositório em memória. Mais adiante pode ser trocado por JPA/banco de dados
 * sem alterar o restante da aplicação.
 */
@Repository
public class JogadorRepository {

    private final Map<Long, Jogador> jogadores = new ConcurrentHashMap<>();
    private final AtomicLong sequencia = new AtomicLong();

    public Jogador criar(String nome, String email, String senhaHash) {
        Jogador jogador = new Jogador(sequencia.incrementAndGet(), nome, email, senhaHash);
        jogadores.put(jogador.id(), jogador);
        return jogador;
    }

    public Optional<Jogador> buscarPorId(Long id) {
        return Optional.ofNullable(jogadores.get(id));
    }

    public boolean existePorEmail(String email) {
        return jogadores.values().stream().anyMatch(j -> j.email().equals(email));
    }
}
