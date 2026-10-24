package com.poker.comum;

import org.springframework.http.HttpStatus;

/**
 * Erro de negócio que vira uma resposta HTTP com o status informado.
 */
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(HttpStatus status, String mensagem) {
        super(mensagem);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
