package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.RestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private RestauranteService restauranteService;

    @GetMapping
    public List<Restaurante> listar() {
        return restauranteRepository.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurante> buscar(@PathVariable Long id) {
        Restaurante restaurante = restauranteRepository.buscar(id);
        if (restaurante != null) {
            return ResponseEntity.ok(restaurante);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> adicionar(@RequestBody Restaurante restaurante) {
        try {
            restaurante = restauranteService.salvar(restaurante);
            return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Restaurante restaurante) {
        Restaurante restauranteAtual = restauranteRepository.buscar(id);
        if (restauranteAtual != null) {
            try {
                BeanUtils.copyProperties(restaurante, restauranteAtual, "id");
                restauranteAtual = restauranteService.salvar(restauranteAtual);
                return ResponseEntity.ok(restauranteAtual);
            } catch (EntidadeNaoEncontradaException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> campos) {
        Restaurante restauranteAtual = restauranteRepository.buscar(id);

        if (restauranteAtual == null) {
            return ResponseEntity.notFound().build();
        }

        merge(campos, restauranteAtual);

        return atualizar(id, restauranteAtual);
    }

    private void merge(Map<String, Object> camposOrigem, Restaurante restauranteDestino) {
        ObjectMapper objectMapper = new ObjectMapper();
        Restaurante restauranteOrigem = objectMapper.convertValue(camposOrigem, Restaurante.class);
        camposOrigem.forEach((nomePropriedade, valorPropriedade) -> {
            Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
            field.setAccessible(true);
            Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
            ReflectionUtils.setField(field, restauranteDestino, valorPropriedade);
        });
    }

}
