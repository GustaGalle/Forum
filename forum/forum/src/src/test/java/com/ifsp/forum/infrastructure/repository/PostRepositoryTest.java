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
class PostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PostRepository postRepository;

    private Usuario usuario;
    private Topico topico;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .nome("Usuário Teste")
                .email("usuario@teste.com")
                .password("senha123")
                .tipo(TipoUsuario.PROFESSOR)
                .build();
        entityManager.persistAndFlush(usuario);

        Forum forum = Forum.builder()
                .nome("Fórum de Teste")
                .descricao("Descrição")
                .build();
        entityManager.persistAndFlush(forum);

        topico = Topico.builder()
                .titulo("Tópico de Teste")
                .descricao("Descrição do tópico")
                .forum(forum)
                .build();
        entityManager.persistAndFlush(topico);
    }

    @Test
    void testSalvarPost() {
        Post post = Post.builder()
                .conteudo("Conteúdo do post")
                .descricao("Descrição do post")
                .usuario(usuario)
                .topico(topico)
                .build();

        Post salvo = postRepository.save(post);
        assertNotNull(salvo.getId());
        assertEquals("Conteúdo do post", salvo.getConteudo());
    }

    @Test
    void testBuscarPostPorId() {
        Post post = Post.builder()
                .conteudo("Conteúdo")
                .descricao("Descrição")
                .usuario(usuario)
                .topico(topico)
                .build();
        Post salvo = entityManager.persistAndFlush(post);

        Optional<Post> encontrado = postRepository.findById(salvo.getId());
        assertTrue(encontrado.isPresent());
        assertEquals("Conteúdo", encontrado.get().getConteudo());
    }

    @Test
    void testListarTodosPosts() {
        Post post1 = Post.builder().conteudo("Post 1").descricao("Desc 1").usuario(usuario).topico(topico).build();
        Post post2 = Post.builder().conteudo("Post 2").descricao("Desc 2").usuario(usuario).topico(topico).build();
        entityManager.persistAndFlush(post1);
        entityManager.persistAndFlush(post2);

        assertEquals(2, postRepository.findAll().size());
    }

    @Test
    void testBuscarPostsPorTopico() {
        Post post1 = Post.builder().conteudo("Post 1").descricao("Desc 1").usuario(usuario).topico(topico).build();
        Post post2 = Post.builder().conteudo("Post 2").descricao("Desc 2").usuario(usuario).topico(topico).build();
        entityManager.persistAndFlush(post1);
        entityManager.persistAndFlush(post2);

        List<Post> posts = postRepository.findByTopicoId(topico.getId());
        assertEquals(2, posts.size());
    }

    @Test
    void testBuscarPostsPorUsuario() {
        Post post1 = Post.builder().conteudo("Post 1").descricao("Desc 1").usuario(usuario).topico(topico).build();
        Post post2 = Post.builder().conteudo("Post 2").descricao("Desc 2").usuario(usuario).topico(topico).build();
        entityManager.persistAndFlush(post1);
        entityManager.persistAndFlush(post2);

        List<Post> posts = postRepository.findByUsuarioId(usuario.getId());
        assertEquals(2, posts.size());
    }

    @Test
    void testAtualizarPost() {
        Post post = Post.builder()
                .conteudo("Conteúdo Original")
                .descricao("Descrição original")
                .usuario(usuario)
                .topico(topico)
                .build();
        Post salvo = entityManager.persistAndFlush(post);

        salvo.setConteudo("Conteúdo Atualizado");
        postRepository.saveAndFlush(salvo);

        Post atualizado = postRepository.findById(salvo.getId()).orElseThrow();
        assertEquals("Conteúdo Atualizado", atualizado.getConteudo());
    }

    @Test
    void testDeletarPost() {
        Post post = Post.builder()
                .conteudo("Post para Deletar")
                .descricao("Descrição")
                .usuario(usuario)
                .topico(topico)
                .build();
        Post salvo = entityManager.persistAndFlush(post);

        postRepository.delete(salvo);
        entityManager.flush();

        assertFalse(postRepository.existsById(salvo.getId()));
    }
}

