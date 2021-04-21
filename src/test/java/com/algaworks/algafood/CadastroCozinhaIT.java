package com.algaworks.algafood;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CozinhaService;
import com.algaworks.algafood.util.DatabaseCleaner;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.Matchers.*;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CadastroCozinhaIT {
    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @BeforeEach
    public void setUp(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/cozinhas";

        databaseCleaner.clearTables();

        prepararDados();
    }


    @Autowired
    private CozinhaService cozinhaService;

    @Test
    public void deveRetornarStatus200_QuandoConsultarCozinhas(){
            given()
                .accept(ContentType.JSON)
            .when()
                .get()
            .then()
                .statusCode(200);
    }

    @Test
    public void deveRetornar2Cozinhas_QuandoConsultarCozinhas(){
        given()
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .body("", Matchers.hasSize(2))
        .body("nome", Matchers.hasItems("Americana", "Tailandesa"));
    }

    @Test
    public void deveRetornarStatus201_QuandoCadastrarCozinha(){
        given()
                .body("{ \"nome\": \"Chinesa\" }")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
            .when()
                .post()
            .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void deveRetornarRespostaEStatusCorretos_QuandoConsultarCozinhaExistente(){
        given()
                .pathParam("cozinhaId", 2)
                .accept(ContentType.JSON)
            .when()
                .get("/{cozinhaId")
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", equalTo("Americana"));
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
        Restaurante restaurante = new Restaurante();
        restaurante.setCozinha(novaCozinha);
        restaurante.setNome("Teste");
        restaurante.setTaxaFrete(BigDecimal.valueOf(10));
        restauranteRepository.save(restaurante);
        Assertions.assertThrows(EntidadeEmUsoException.class, () -> cozinhaService.excluir(3L) );

    }

    @Test
    public void deveFalhar_QuandoExcluirCozinhaInexistente() {
        Assertions.assertThrows(CozinhaNaoEncontradaException.class, () -> cozinhaService.excluir(8L) );
    }

    private void prepararDados(){
        Cozinha cozinha1 = new Cozinha();
        cozinha1.setNome("Tailandesa");
        cozinhaRepository.save(cozinha1);

        Cozinha cozinha2 = new Cozinha();
        cozinha2.setNome("Americana");
        cozinhaRepository.save(cozinha2);
    }
}
