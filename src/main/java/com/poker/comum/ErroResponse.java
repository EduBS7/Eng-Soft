package com.poker.comum;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Formato padrão de todas as respostas de erro da API.
 */
public record ErroResponse(
        int status,
        String erro,
        String mensagem,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<String> detalhes) {

    public static ErroResponse de(HttpStatus status, String mensagem) {
        return new ErroResponse(status.value(), status.getReasonPhrase(), mensagem, List.of());
    }

    public static ErroResponse de(HttpStatus status, String mensagem, List<String> detalhes) {
        return new ErroResponse(status.value(), status.getReasonPhrase(), mensagem, detalhes);
    }
}
