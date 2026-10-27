package com.poker.partida;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/poker")
public class PartidaController {

    private final PartidaService partidaService;

    public PartidaController(PartidaService partidaService) {
        this.partidaService = partidaService;
    }

    /**
     * Endpoint de apoio (não faz parte das histórias iniciais): sem ele não existe
     * como criar mesas para testar US09, US10 e US12.
     */
    @PostMapping("/partidas")
    public ResponseEntity<CriarPartidaResponse> criar(@Valid @RequestBody CriarPartidaRequest requisicao) {
        Partida partida = partidaService.criar(requisicao.idJogador(), requisicao.nome(), requisicao.maxJogadores());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CriarPartidaResponse(partida.getId()));
    }

    /** US09 – Consulta de partidas disponíveis. */
    @GetMapping("/partidasAbertas")
    public List<PartidaResumoResponse> listarAbertas() {
        return partidaService.listarAbertas().stream()
                .map(PartidaResumoResponse::de)
                .toList();
    }
}
