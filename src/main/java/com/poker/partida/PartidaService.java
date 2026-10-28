package com.poker.partida;

import com.poker.comum.ApiException;
import com.poker.jogador.JogadorService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartidaService {

    private final PartidaRepository repositorio;
    private final JogadorService jogadorService;

    public PartidaService(PartidaRepository repositorio, JogadorService jogadorService) {
        this.repositorio = repositorio;
        this.jogadorService = jogadorService;
    }

    /** Cria a mesa; o criador já entra como primeiro jogador e é o único que pode iniciá-la. */
    public Partida criar(Long idCriador, String nome, Integer maxJogadores) {
        jogadorService.buscarPorId(idCriador); // 404 se o jogador não existe

        int max = maxJogadores == null ? Partida.MAX_JOGADORES_PADRAO : maxJogadores;
        if (max < Partida.MIN_JOGADORES || max > Partida.MAX_JOGADORES_LIMITE) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "maxJogadores deve estar entre "
                    + Partida.MIN_JOGADORES + " e " + Partida.MAX_JOGADORES_LIMITE + ".");
        }
        return repositorio.criar(nome.trim(), idCriador, max);
    }

    /** US09 – partidas que ainda não começaram e ainda têm vaga. */
    public List<Partida> listarAbertas() {
        return repositorio.listarTodas().stream()
                .filter(Partida::estaAberta)
                .toList();
    }

    public Partida buscarPorId(Long idPartida) {
        return repositorio.buscarPorId(idPartida)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Partida não encontrada: " + idPartida));
    }
}
