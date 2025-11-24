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
class ComentarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ComentarioRepository comentarioRepository;

    private Usuario usuario;
    private Post post;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .nome("Usuário Teste")
                .email("usuario@teste.com")
                .password("senha123")
                .tipo(TipoUsuario.ALUNO)
                .build();
        entityManager.persistAndFlush(usuario);

        Forum forum = Forum.builder()
                .nome("Fórum de Teste")
                .descricao("Descrição")
                .build();
        entityManager.persistAndFlush(forum);

        Topico topico = Topico.builder()
                .titulo("Tópico de Teste")
                .descricao("Descrição")
                .forum(forum)
                .build();
        entityManager.persistAndFlush(topico);

        post = Post.builder()
                .conteudo("Conteúdo do post")
                .descricao("Descrição")
                .usuario(usuario)
                .topico(topico)
                .build();
        entityManager.persistAndFlush(post);
    }

    @Test
    void testSalvarComentario() {
        Comentario comentario = Comentario.builder()
                .mensagem("Comentário de teste")
                .usuario(usuario)
                .post(post)
                .build();

        Comentario salvo = comentarioRepository.save(comentario);
        assertNotNull(salvo.getId());
        assertEquals("Comentário de teste", salvo.getMensagem());
    }

    @Test
    void testBuscarComentarioPorId() {
        Comentario comentario = Comentario.builder()
                .mensagem("Comentário")
                .usuario(usuario)
                .post(post)
                .build();
        Comentario salvo = entityManager.persistAndFlush(comentario);

        Optional<Comentario> encontrado = comentarioRepository.findById(salvo.getId());
        assertTrue(encontrado.isPresent());
        assertEquals("Comentário", encontrado.get().getMensagem());
    }

    @Test
    void testListarTodosComentarios() {
        Comentario comentario1 = Comentario.builder().mensagem("Comentário 1").usuario(usuario).post(post).build();
        Comentario comentario2 = Comentario.builder().mensagem("Comentário 2").usuario(usuario).post(post).build();
        entityManager.persistAndFlush(comentario1);
        entityManager.persistAndFlush(comentario2);

        assertEquals(2, comentarioRepository.findAll().size());
    }

    @Test
    void testBuscarComentariosPorPost() {
        Comentario comentario1 = Comentario.builder().mensagem("Comentário 1").usuario(usuario).post(post).build();
        Comentario comentario2 = Comentario.builder().mensagem("Comentário 2").usuario(usuario).post(post).build();
        entityManager.persistAndFlush(comentario1);
        entityManager.persistAndFlush(comentario2);

        List<Comentario> comentarios = comentarioRepository.findByPostId(post.getId());
        assertEquals(2, comentarios.size());
    }

    @Test
    void testBuscarComentariosPorUsuario() {
        Comentario comentario1 = Comentario.builder().mensagem("Comentário 1").usuario(usuario).post(post).build();
        Comentario comentario2 = Comentario.builder().mensagem("Comentário 2").usuario(usuario).post(post).build();
        entityManager.persistAndFlush(comentario1);
        entityManager.persistAndFlush(comentario2);

        List<Comentario> comentarios = comentarioRepository.findByUsuarioId(usuario.getId());
        assertEquals(2, comentarios.size());
    }

    @Test
    void testAtualizarComentario() {
        Comentario comentario = Comentario.builder()
                .mensagem("Mensagem Original")
                .usuario(usuario)
                .post(post)
                .build();
        Comentario salvo = entityManager.persistAndFlush(comentario);

        salvo.setMensagem("Mensagem Atualizada");
        comentarioRepository.saveAndFlush(salvo);

        Comentario atualizado = comentarioRepository.findById(salvo.getId()).orElseThrow();
        assertEquals("Mensagem Atualizada", atualizado.getMensagem());
    }

    @Test
    void testDeletarComentario() {
        Comentario comentario = Comentario.builder()
                .mensagem("Comentário para Deletar")
                .usuario(usuario)
                .post(post)
                .build();
        Comentario salvo = entityManager.persistAndFlush(comentario);

        comentarioRepository.delete(salvo);
        entityManager.flush();

        assertFalse(comentarioRepository.existsById(salvo.getId()));
    }
}

