package com.poker.rodada;

import com.poker.carta.Baralho;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RodadaService {

    private final DistribuidorDeCartas distribuidor;

    public RodadaService(DistribuidorDeCartas distribuidor) {
        this.distribuidor = distribuidor;
    }

    /** Embaralha um baralho novo e distribui as cartas aos jogadores informados. */
    public Rodada distribuirCartas(int numeroRodada, List<Long> idsJogadores) {
        return distribuidor.iniciarRodada(numeroRodada, idsJogadores, new Baralho());
    }
}
