package com.ifsp.forum.infrastructure.repository;

import com.ifsp.forum.infrastructure.entidys.Forum;
import com.ifsp.forum.infrastructure.entidys.TipoUsuario;
import com.ifsp.forum.infrastructure.entidys.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ForumRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ForumRepository forumRepository;

    private Usuario usuarioProfessor;
    private Usuario usuarioAlunoVeterano;

    @BeforeEach
    void setUp() {
        usuarioProfessor = Usuario.builder()
                .nome("Professor Teste")
                .email("professor@teste.com")
                .password("senha123")
                .tipo(TipoUsuario.PROFESSOR)
                .build();
        entityManager.persistAndFlush(usuarioProfessor);

        usuarioAlunoVeterano = Usuario.builder()
                .nome("Aluno Veterano Teste")
                .email("aluno.veterano@teste.com")
                .password("senha123")
                .tipo(TipoUsuario.ALUNO_VETERANO)
                .build();
        entityManager.persistAndFlush(usuarioAlunoVeterano);
    }

    @Test
    void testSalvarForum() {
        Forum forum = Forum.builder()
                .nome("Fórum de Teste")
                .descricao("Descrição do fórum de teste")
                .build();

        Forum salvo = forumRepository.save(forum);
        assertNotNull(salvo.getId());
        assertEquals("Fórum de Teste", salvo.getNome());
    }

    @Test
    void testBuscarForumPorId() {
        Forum forum = Forum.builder()
                .nome("Fórum de Teste")
                .descricao("Descrição do fórum")
                .build();
        Forum salvo = entityManager.persistAndFlush(forum);

        Optional<Forum> encontrado = forumRepository.findById(salvo.getId());
        assertTrue(encontrado.isPresent());
        assertEquals("Fórum de Teste", encontrado.get().getNome());
    }

    @Test
    void testListarTodosForuns() {
        Forum forum1 = Forum.builder().nome("Fórum 1").descricao("Desc 1").build();
        Forum forum2 = Forum.builder().nome("Fórum 2").descricao("Desc 2").build();
        entityManager.persistAndFlush(forum1);
        entityManager.persistAndFlush(forum2);

        assertEquals(2, forumRepository.findAll().size());
    }

    @Test
    void testAtualizarForum() {
        Forum forum = Forum.builder()
                .nome("Fórum Original")
                .descricao("Descrição original")
                .build();
        Forum salvo = entityManager.persistAndFlush(forum);

        salvo.setNome("Fórum Atualizado");
        forumRepository.saveAndFlush(salvo);

        Forum atualizado = forumRepository.findById(salvo.getId()).orElseThrow();
        assertEquals("Fórum Atualizado", atualizado.getNome());
    }

    @Test
    void testDeletarForum() {
        Forum forum = Forum.builder()
                .nome("Fórum para Deletar")
                .descricao("Descrição")
                .build();
        Forum salvo = entityManager.persistAndFlush(forum);

        forumRepository.delete(salvo);
        entityManager.flush();

        assertFalse(forumRepository.existsById(salvo.getId()));
    }
}

