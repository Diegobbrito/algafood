package com.algaworks.algafood.domain.exception;

public class CozinhaNaoEncontradoException extends EntidadeNaoEncontradaException {
    public CozinhaNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
    public CozinhaNaoEncontradoException(Long id) {
        this(String.format("Não existe um cadastro de Cozinha com o código %d.", id));
    }
}
