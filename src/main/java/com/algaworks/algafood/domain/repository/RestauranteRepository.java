package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

//    @Query("from Restaurante where nome like %:nome% and cozinha.id = :id")
    List<Restaurante> consultarPorNome(String nome, @Param("id") Long cozinha);

    List<Restaurante> findByTaxaFreteBetween (BigDecimal taxaInitial, BigDecimal taxaFinal);
    List<Restaurante> findByNomeContainingAndCozinhaId(String nome, Long cozinha);
    List<Restaurante> findTop2ByNomeContaining(String nome);
    int countByCozinhaId(Long cozinha);
}
