package com.poker.partida;

import com.poker.comum.ApiException;
import com.poker.jogador.JogadorService;
import com.poker.rodada.Rodada;
import com.poker.rodada.RodadaService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Regras de quem senta à mesa (US10) e de quando o jogo começa (US12).
 */
@Service
public class ParticipacaoService {

    private final PartidaService partidaService;
    private final JogadorService jogadorService;
    private final RodadaService rodadaService;

    public ParticipacaoService(PartidaService partidaService,
                               JogadorService jogadorService,
                               RodadaService rodadaService) {
        this.partidaService = partidaService;
        this.jogadorService = jogadorService;
        this.rodadaService = rodadaService;
    }

    /** US10 – Entrada em uma partida. */
    public Partida entrar(Long idPartida, Long idJogador) {
        Partida partida = partidaService.buscarPorId(idPartida);   // 404
        jogadorService.buscarPorId(idJogador);                     // 404

        // Verificar e adicionar precisam ser atômicos, senão duas pessoas ocupam a última vaga.
        synchronized (partida) {
            if (partida.getStatus() != StatusPartida.AGUARDANDO) {
                throw new ApiException(HttpStatus.CONFLICT, "A partida já foi iniciada.");
            }
            if (partida.temJogador(idJogador)) {
                throw new ApiException(HttpStatus.CONFLICT, "O jogador já está nesta partida.");
            }
            if (partida.estaCheia()) {
                throw new ApiException(HttpStatus.CONFLICT, "A mesa está cheia.");
            }
            partida.adicionarJogador(idJogador);
        }
        return partida;
    }

    /** US12 – Início de uma partida (e US13: distribui as cartas da primeira rodada). */
    public Partida iniciar(Long idPartida, Long idJogador) {
        Partida partida = partidaService.buscarPorId(idPartida);   // 404

        synchronized (partida) {
            if (!partida.getIdCriador().equals(idJogador)) {
                throw new ApiException(HttpStatus.FORBIDDEN,
                        "Somente o jogador que criou a mesa pode iniciar a partida.");
            }
            if (partida.getStatus() != StatusPartida.AGUARDANDO) {
                throw new ApiException(HttpStatus.CONFLICT, "A partida já foi iniciada.");
            }
            if (partida.getQuantidadeJogadores() < Partida.MIN_JOGADORES) {
                throw new ApiException(HttpStatus.CONFLICT, "São necessários pelo menos "
                        + Partida.MIN_JOGADORES + " jogadores para iniciar. Presentes: "
                        + partida.getQuantidadeJogadores() + ".");
            }
            Rodada primeiraRodada = rodadaService.distribuirCartas(1, partida.getJogadores());
            partida.iniciar(primeiraRodada);
        }
        return partida;
    }
}
