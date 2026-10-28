package com.poker.partida;

import com.poker.rodada.Rodada;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Mesa de pôquer. Operações que checam e depois alteram o estado (entrar, iniciar)
 * devem ser feitas dentro de synchronized(partida) nos serviços.
 */
public class Partida {

    public static final int MIN_JOGADORES = 2;
    public static final int MAX_JOGADORES_LIMITE = 10;
    public static final int MAX_JOGADORES_PADRAO = 6;

    private final Long id;
    private final String nome;
    private final Long idCriador;
    private final int maxJogadores;
    private final List<Long> jogadores = new CopyOnWriteArrayList<>();
    private volatile StatusPartida status = StatusPartida.AGUARDANDO;
    private volatile Rodada rodadaAtual;

    public Partida(Long id, String nome, Long idCriador, int maxJogadores) {
        this.id = id;
        this.nome = nome;
        this.idCriador = idCriador;
        this.maxJogadores = maxJogadores;
        this.jogadores.add(idCriador); // o criador já senta à mesa
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Long getIdCriador() {
        return idCriador;
    }

    public int getMaxJogadores() {
        return maxJogadores;
    }

    public StatusPartida getStatus() {
        return status;
    }

    public Rodada getRodadaAtual() {
        return rodadaAtual;
    }

    /** Cópia imutável, na ordem em que os jogadores sentaram. */
    public List<Long> getJogadores() {
        return List.copyOf(jogadores);
    }

    public int getQuantidadeJogadores() {
        return jogadores.size();
    }

    public boolean temJogador(Long idJogador) {
        return jogadores.contains(idJogador);
    }

    public boolean estaCheia() {
        return jogadores.size() >= maxJogadores;
    }

    /** Aberta = ainda não começou e ainda tem lugar. */
    public boolean estaAberta() {
        return status == StatusPartida.AGUARDANDO && !estaCheia();
    }

    public void adicionarJogador(Long idJogador) {
        if (estaCheia()) {
            throw new IllegalStateException("A mesa está cheia.");
        }
        jogadores.add(idJogador);
    }

    /** A rodada é gravada antes do status para que quem vê EM_ANDAMENTO sempre veja a rodada. */
    public void iniciar(Rodada primeiraRodada) {
        this.rodadaAtual = primeiraRodada;
        this.status = StatusPartida.EM_ANDAMENTO;
    }
}
