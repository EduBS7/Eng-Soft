package com.poker.jogador;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/poker")
public class JogadorController {

    private final JogadorService jogadorService;

    public JogadorController(JogadorService jogadorService) {
        this.jogadorService = jogadorService;
    }

    /** US01 – Cadastro de jogadores. */
    @PostMapping("/cadastro")
    public ResponseEntity<CadastroJogadorResponse> cadastrar(@Valid @RequestBody CadastroJogadorRequest requisicao) {
        Jogador jogador = jogadorService.cadastrar(requisicao.nome(), requisicao.email(), requisicao.senha());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CadastroJogadorResponse(jogador.id()));
    }
}
