package com.algaworks.algafood;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.service.CozinhaService;
import static org.assertj.core.api.Assertions.*;

import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@SpringBootTest
class CadastroCozinhaIntegrationTests {

    @Autowired
    private CozinhaService cozinhaService;

    @Test
    public void testarCadastroCozinhaComSucesso(){
        Cozinha novaCozinha = new Cozinha();
        novaCozinha.setNome("Chinesa");

        novaCozinha = cozinhaService.salvar(novaCozinha);

        assertThat(novaCozinha).isNotNull();
        assertThat(novaCozinha.getId()).isNotNull();

    }

    @Test
    public void testarCadastroCozinhaSemNome(){
        Cozinha novaCozinha = new Cozinha();
        novaCozinha.setNome(null);

        Assertions.assertThrows(ConstraintViolationException.class, () -> cozinhaService.salvar(novaCozinha));
    }



}
