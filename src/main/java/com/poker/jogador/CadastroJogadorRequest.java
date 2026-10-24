package com.poker.jogador;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CadastroJogadorRequest(
        @NotBlank(message = "o nome é obrigatório")
        @Size(max = 100, message = "o nome deve ter no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "o e-mail é obrigatório")
        @Email(message = "o e-mail é inválido")
        String email,

        @NotBlank(message = "a senha é obrigatória")
        @Size(min = 6, max = 72, message = "a senha deve ter entre 6 e 72 caracteres")
        String senha) {
}
