package com.algaworks.algafood.domain.exception;

public class EstadoNaoEncontradoException extends EntidadeNaoEncontradaException {
    public EstadoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
    public EstadoNaoEncontradoException(Long id) {
        this(String.format("Não existe um cadastro de Estado com o código %d.", id));
    }
}
