package com.poker.comum;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

/**
 * Converte exceções em respostas de erro padronizadas.
 */
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErroResponse> tratarApiException(ApiException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(ErroResponse.de(ex.getStatus(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratarValidacao(MethodArgumentNotValidException ex) {
        List<String> detalhes = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .toList();
        return ResponseEntity.badRequest()
                .body(ErroResponse.de(HttpStatus.BAD_REQUEST, "Dados inválidos.", detalhes));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> tratarCorpoIlegivel(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(ErroResponse.de(HttpStatus.BAD_REQUEST,
                "Corpo da requisição ausente ou JSON malformado."));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErroResponse> tratarParametroAusente(MissingServletRequestParameterException ex) {
        return ResponseEntity.badRequest().body(ErroResponse.de(HttpStatus.BAD_REQUEST,
                "Parâmetro obrigatório ausente: " + ex.getParameterName()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> tratarTipoInvalido(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest().body(ErroResponse.de(HttpStatus.BAD_REQUEST,
                "Valor inválido para o parâmetro: " + ex.getName()));
    }
}
