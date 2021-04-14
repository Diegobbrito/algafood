package com.algaworks.algafood;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.service.CozinhaService;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CadastroCozinhaIT {

    @LocalServerPort
    private int port;

    @Autowired
    private CozinhaService cozinhaService;

    @Test
    public void deveRetornarStatus200_QaundoConsultarCozinhas(){
            given()
                .basePath("/cozinhas")
                .port(port)
                .accept(ContentType.JSON)
            .when()
                .get()
            .then()
                .statusCode(200);
    }

    @Test
    public void deveAtribuirId_QuandoCadastrarCozinhaComDadosCorretos(){
        Cozinha novaCozinha = new Cozinha();
        novaCozinha.setNome("Chinesa");

        novaCozinha = cozinhaService.salvar(novaCozinha);

        assertThat(novaCozinha).isNotNull();
        assertThat(novaCozinha.getId()).isNotNull();

    }

    @Test
    public void deveFalhar_QuandoCadastrarCozinhaSemNome(){
        Cozinha novaCozinha = new Cozinha();
        novaCozinha.setNome(null);
        Assertions.assertThrows(ConstraintViolationException.class, () -> cozinhaService.salvar(novaCozinha));
    }

    @Test
    public void deveFalhar_QuandoExcluirCozinhaEmUso(){
        Cozinha novaCozinha = new Cozinha();
        novaCozinha.setNome("Chinesa");
        cozinhaService.salvar(novaCozinha);
        Assertions.assertThrows(EntidadeEmUsoException.class, () -> cozinhaService.excluir(1L) );

    }
    @Test
    public void deveFalhar_QuandoExcluirCozinhaInexistente() {
        Assertions.assertThrows(CozinhaNaoEncontradaException.class, () -> cozinhaService.excluir(8L) );
    }
}
