package com.ifsp.forum.infrastructure.repository;

import com.ifsp.forum.infrastructure.entidys.Forum;
import com.ifsp.forum.infrastructure.entidys.Topico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TopicoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TopicoRepository topicoRepository;

    private Forum forum;

    @BeforeEach
    void setUp() {
        forum = Forum.builder()
                .nome("Fórum de Teste")
                .descricao("Descrição do fórum")
                .build();
        entityManager.persistAndFlush(forum);
    }

    @Test
    void testSalvarTopico() {
        Topico topico = Topico.builder()
                .titulo("Tópico de Teste")
                .descricao("Descrição do tópico")
                .forum(forum)
                .build();

        Topico salvo = topicoRepository.save(topico);
        assertNotNull(salvo.getId());
        assertEquals("Tópico de Teste", salvo.getTitulo());
    }

    @Test
    void testBuscarTopicoPorId() {
        Topico topico = Topico.builder()
                .titulo("Tópico de Teste")
                .descricao("Descrição")
                .forum(forum)
                .build();
        Topico salvo = entityManager.persistAndFlush(topico);

        Optional<Topico> encontrado = topicoRepository.findById(salvo.getId());
        assertTrue(encontrado.isPresent());
        assertEquals("Tópico de Teste", encontrado.get().getTitulo());
    }

    @Test
    void testListarTodosTopicos() {
        Topico topico1 = Topico.builder().titulo("Tópico 1").descricao("Desc 1").forum(forum).build();
        Topico topico2 = Topico.builder().titulo("Tópico 2").descricao("Desc 2").forum(forum).build();
        entityManager.persistAndFlush(topico1);
        entityManager.persistAndFlush(topico2);

        assertEquals(2, topicoRepository.findAll().size());
    }

    @Test
    void testBuscarTopicosPorForum() {
        Topico topico1 = Topico.builder().titulo("Tópico 1").descricao("Desc 1").forum(forum).build();
        Topico topico2 = Topico.builder().titulo("Tópico 2").descricao("Desc 2").forum(forum).build();
        entityManager.persistAndFlush(topico1);
        entityManager.persistAndFlush(topico2);

        List<Topico> topicos = topicoRepository.findByForumId(forum.getId());
        assertEquals(2, topicos.size());
    }

    @Test
    void testAtualizarTopico() {
        Topico topico = Topico.builder()
                .titulo("Tópico Original")
                .descricao("Descrição original")
                .forum(forum)
                .build();
        Topico salvo = entityManager.persistAndFlush(topico);

        salvo.setTitulo("Tópico Atualizado");
        topicoRepository.saveAndFlush(salvo);

        Topico atualizado = topicoRepository.findById(salvo.getId()).orElseThrow();
        assertEquals("Tópico Atualizado", atualizado.getTitulo());
    }

    @Test
    void testDeletarTopico() {
        Topico topico = Topico.builder()
                .titulo("Tópico para Deletar")
                .descricao("Descrição")
                .forum(forum)
                .build();
        Topico salvo = entityManager.persistAndFlush(topico);

        topicoRepository.delete(salvo);
        entityManager.flush();

        assertFalse(topicoRepository.existsById(salvo.getId()));
    }
}

