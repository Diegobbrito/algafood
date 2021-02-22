package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.Restaurante;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RestauranteRepository extends CustomJpaRepository<Restaurante, Long>, RestauranteRepositoryQueries, JpaSpecificationExecutor<Restaurante> {

//    @Query("from Restaurante r join r.cozinha left join fetch r.formasPagamento")
    @Query("from Restaurante r join r.cozinha")
    List<Restaurante> findAll();

//    @Query("from Restaurante where nome like %:nome% and cozinha.id = :id")
    List<Restaurante> consultarPorNome(String nome, @Param("id") Long cozinha);
    List<Restaurante> findByNomeContainingAndCozinhaId(String nome, Long cozinha);
    List<Restaurante> findByTaxaFreteBetween (BigDecimal taxaInitial, BigDecimal taxaFinal);
    List<Restaurante> findTop2ByNomeContaining(String nome);
    int countByCozinhaId(Long cozinha);
}
