package com.ifsp.forum.infrastructure.repository;

import com.ifsp.forum.infrastructure.entidys.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SubmissaoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SubmissaoRepository submissaoRepository;

    private Usuario usuario;
    private Algoritmo algoritmo;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .nome("Usuário Teste")
                .email("usuario@teste.com")
                .password("senha123")
                .tipo(TipoUsuario.ALUNO)
                .build();
        entityManager.persistAndFlush(usuario);

        algoritmo = Algoritmo.builder()
                .titulo("Algoritmo de Teste")
                .descricao("Descrição")
                .linguagemPermitida("java")
                .judge0LanguageId(62)
                .build();
        entityManager.persistAndFlush(algoritmo);
    }

    @Test
    void testSalvarSubmissao() {
        Submissao submissao = Submissao.builder()
                .usuario(usuario)
                .algoritmo(algoritmo)
                .codigo("public class Main { public static void main(String[] args) { } }")
                .aprovado(false)
                .feedback("Teste falhou")
                .build();

        Submissao salva = submissaoRepository.save(submissao);
        assertNotNull(salva.getId());
        assertNotNull(salva.getCodigo());
    }

    @Test
    void testBuscarSubmissaoPorId() {
        Submissao submissao = Submissao.builder()
                .usuario(usuario)
                .algoritmo(algoritmo)
                .codigo("código de teste")
                .build();
        Submissao salva = entityManager.persistAndFlush(submissao);

        Optional<Submissao> encontrada = submissaoRepository.findById(salva.getId());
        assertTrue(encontrada.isPresent());
        assertEquals("código de teste", encontrada.get().getCodigo());
    }

    @Test
    void testListarTodasSubmissoes() {
        Submissao submissao1 = Submissao.builder()
                .usuario(usuario)
                .algoritmo(algoritmo)
                .codigo("código 1")
                .build();
        Submissao submissao2 = Submissao.builder()
                .usuario(usuario)
                .algoritmo(algoritmo)
                .codigo("código 2")
                .build();
        entityManager.persistAndFlush(submissao1);
        entityManager.persistAndFlush(submissao2);

        assertEquals(2, submissaoRepository.findAll().size());
    }

    @Test
    void testBuscarSubmissoesPorUsuario() {
        Submissao submissao1 = Submissao.builder()
                .usuario(usuario)
                .algoritmo(algoritmo)
                .codigo("código 1")
                .build();
        Submissao submissao2 = Submissao.builder()
                .usuario(usuario)
                .algoritmo(algoritmo)
                .codigo("código 2")
                .build();
        entityManager.persistAndFlush(submissao1);
        entityManager.persistAndFlush(submissao2);

        List<Submissao> submissoes = submissaoRepository.findByUsuarioId(usuario.getId());
        assertEquals(2, submissoes.size());
    }

    @Test
    void testBuscarSubmissoesPorAlgoritmo() {
        Submissao submissao1 = Submissao.builder()
                .usuario(usuario)
                .algoritmo(algoritmo)
                .codigo("código 1")
                .build();
        Submissao submissao2 = Submissao.builder()
                .usuario(usuario)
                .algoritmo(algoritmo)
                .codigo("código 2")
                .build();
        entityManager.persistAndFlush(submissao1);
        entityManager.persistAndFlush(submissao2);

        List<Submissao> submissoes = submissaoRepository.findByAlgoritmoId(algoritmo.getId());
        assertEquals(2, submissoes.size());
    }

    @Test
    void testAtualizarSubmissao() {
        Submissao submissao = Submissao.builder()
                .usuario(usuario)
                .algoritmo(algoritmo)
                .codigo("código original")
                .aprovado(false)
                .build();
        Submissao salva = entityManager.persistAndFlush(submissao);

        salva.setAprovado(true);
        salva.setFeedback("Teste aprovado");
        submissaoRepository.saveAndFlush(salva);

        Submissao atualizada = submissaoRepository.findById(salva.getId()).orElseThrow();
        assertTrue(atualizada.getAprovado());
        assertEquals("Teste aprovado", atualizada.getFeedback());
    }

    @Test
    void testDeletarSubmissao() {
        Submissao submissao = Submissao.builder()
                .usuario(usuario)
                .algoritmo(algoritmo)
                .codigo("código para deletar")
                .build();
        Submissao salva = entityManager.persistAndFlush(submissao);

        submissaoRepository.delete(salva);
        entityManager.flush();

        assertFalse(submissaoRepository.existsById(salva.getId()));
    }
}

