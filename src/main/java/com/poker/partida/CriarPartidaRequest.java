package com.poker.partida;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CriarPartidaRequest(
        @NotNull(message = "o idJogador é obrigatório")
        Long idJogador,

        @NotBlank(message = "o nome da mesa é obrigatório")
        @Size(max = 50, message = "o nome da mesa deve ter no máximo 50 caracteres")
        String nome,

        /** Opcional: se não informado, a mesa terá 6 lugares. */
        @Min(value = 2, message = "a mesa precisa de pelo menos 2 lugares")
        @Max(value = 10, message = "a mesa pode ter no máximo 10 lugares")
        Integer maxJogadores) {
}
