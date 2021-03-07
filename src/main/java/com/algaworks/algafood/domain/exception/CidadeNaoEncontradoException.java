package com.algaworks.algafood.domain.exception;

public class CidadeNaoEncontradoException extends EntidadeNaoEncontradaException {
    public CidadeNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
    public CidadeNaoEncontradoException(Long id) {
        this(String.format("Não existe um cadastro de Cidade com o código %d.", id));
    }
}
