package com.poker.partida;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/poker")
public class ParticipacaoController {

    private final ParticipacaoService participacaoService;

    public ParticipacaoController(ParticipacaoService participacaoService) {
        this.participacaoService = participacaoService;
    }

    /** US10 – Entrada em uma partida. */
    @PostMapping("/{idPartida}/entrar")
    public EntrarPartidaResponse entrar(@PathVariable("idPartida") Long idPartida,
                                        @RequestParam("idJogador") Long idJogador) {
        Partida partida = participacaoService.entrar(idPartida, idJogador);
        return new EntrarPartidaResponse(true, "Jogador entrou na partida.", partida.getId(), idJogador,
                partida.getQuantidadeJogadores(), partida.getMaxJogadores());
    }

    /**
     * US12 – Início de uma partida.
     * Como não há login ainda, o jogador que pede o início é informado em idJogador
     * e precisa ser o criador da mesa.
     */
    @PostMapping("/{idPartida}/iniciar")
    public IniciarPartidaResponse iniciar(@PathVariable("idPartida") Long idPartida,
                                          @RequestParam("idJogador") Long idJogador) {
        Partida partida = participacaoService.iniciar(idPartida, idJogador);
        return new IniciarPartidaResponse(true, "Partida iniciada. Cartas distribuídas.", partida.getId(),
                partida.getRodadaAtual().getNumero(), partida.getQuantidadeJogadores());
    }
}
