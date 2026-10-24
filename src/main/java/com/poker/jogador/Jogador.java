package com.poker.jogador;

public record Jogador(Long id, String nome, String email, String senhaHash) {

    /** Não imprime o hash da senha em logs. */
    @Override
    public String toString() {
        return "Jogador[id=" + id + ", nome=" + nome + ", email=" + email + "]";
    }
}
