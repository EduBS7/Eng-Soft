package com.poker.jogador;

import com.poker.comum.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class JogadorService {

    private final JogadorRepository repositorio;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public JogadorService(JogadorRepository repositorio) {
        this.repositorio = repositorio;
    }

    /** synchronized evita que dois cadastros simultâneos usem o mesmo e-mail. */
    public synchronized Jogador cadastrar(String nome, String email, String senha) {
        String emailNormalizado = email.trim().toLowerCase(Locale.ROOT);
        if (repositorio.existePorEmail(emailNormalizado)) {
            throw new ApiException(HttpStatus.CONFLICT, "Já existe um jogador cadastrado com este e-mail.");
        }
        return repositorio.criar(nome.trim(), emailNormalizado, passwordEncoder.encode(senha));
    }

    public Jogador buscarPorId(Long id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Jogador não encontrado: " + id));
    }
}
