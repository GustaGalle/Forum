package com.ifsp.forum.infrastructure.repository;

import com.ifsp.forum.infrastructure.entidys.Algoritmo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AlgoritmoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AlgoritmoRepository algoritmoRepository;

    @Test
    void testSalvarAlgoritmo() {
        Algoritmo algoritmo = Algoritmo.builder()
                .titulo("Algoritmo de Teste")
                .descricao("Descrição do algoritmo")
                .linguagemPermitida("java")
                .gabarito("public class Main { }")
                .judge0LanguageId(62)
                .casosDeTeste("[{\"entrada\":\"1\",\"saidaEsperada\":\"1\"}]")
                .build();

        Algoritmo salvo = algoritmoRepository.save(algoritmo);
        assertNotNull(salvo.getId());
        assertEquals("Algoritmo de Teste", salvo.getTitulo());
    }

    @Test
    void testBuscarAlgoritmoPorId() {
        Algoritmo algoritmo = Algoritmo.builder()
                .titulo("Algoritmo de Teste")
                .descricao("Descrição")
                .linguagemPermitida("java")
                .judge0LanguageId(62)
                .build();
        Algoritmo salvo = entityManager.persistAndFlush(algoritmo);

        Optional<Algoritmo> encontrado = algoritmoRepository.findById(salvo.getId());
        assertTrue(encontrado.isPresent());
        assertEquals("Algoritmo de Teste", encontrado.get().getTitulo());
    }

    @Test
    void testListarTodosAlgoritmos() {
        Algoritmo algoritmo1 = Algoritmo.builder()
                .titulo("Algoritmo 1")
                .descricao("Desc 1")
                .linguagemPermitida("java")
                .judge0LanguageId(62)
                .build();
        Algoritmo algoritmo2 = Algoritmo.builder()
                .titulo("Algoritmo 2")
                .descricao("Desc 2")
                .linguagemPermitida("python")
                .judge0LanguageId(71)
                .build();
        entityManager.persistAndFlush(algoritmo1);
        entityManager.persistAndFlush(algoritmo2);

        assertEquals(2, algoritmoRepository.findAll().size());
    }

    @Test
    void testAtualizarAlgoritmo() {
        Algoritmo algoritmo = Algoritmo.builder()
                .titulo("Algoritmo Original")
                .descricao("Descrição original")
                .linguagemPermitida("java")
                .judge0LanguageId(62)
                .build();
        Algoritmo salvo = entityManager.persistAndFlush(algoritmo);

        salvo.setTitulo("Algoritmo Atualizado");
        algoritmoRepository.saveAndFlush(salvo);

        Algoritmo atualizado = algoritmoRepository.findById(salvo.getId()).orElseThrow();
        assertEquals("Algoritmo Atualizado", atualizado.getTitulo());
    }

    @Test
    void testDeletarAlgoritmo() {
        Algoritmo algoritmo = Algoritmo.builder()
                .titulo("Algoritmo para Deletar")
                .descricao("Descrição")
                .linguagemPermitida("java")
                .judge0LanguageId(62)
                .build();
        Algoritmo salvo = entityManager.persistAndFlush(algoritmo);

        algoritmoRepository.delete(salvo);
        entityManager.flush();

        assertFalse(algoritmoRepository.existsById(salvo.getId()));
    }
}



