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
class CurtidaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CurtidaRepository curtidaRepository;

    private Usuario usuario;
    private Post post;
    private Comentario comentario;

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

        comentario = Comentario.builder()
                .mensagem("Comentário de teste")
                .usuario(usuario)
                .post(post)
                .build();
        entityManager.persistAndFlush(comentario);
    }

    @Test
    void testSalvarCurtidaEmPost() {
        Curtida curtida = Curtida.builder()
                .usuario(usuario)
                .post(post)
                .build();

        Curtida salva = curtidaRepository.save(curtida);
        assertNotNull(salva.getId());
        assertNotNull(salva.getPost());
    }

    @Test
    void testSalvarCurtidaEmComentario() {
        Curtida curtida = Curtida.builder()
                .usuario(usuario)
                .comentario(comentario)
                .build();

        Curtida salva = curtidaRepository.save(curtida);
        assertNotNull(salva.getId());
        assertNotNull(salva.getComentario());
    }

    @Test
    void testBuscarCurtidaPorId() {
        Curtida curtida = Curtida.builder()
                .usuario(usuario)
                .post(post)
                .build();
        Curtida salva = entityManager.persistAndFlush(curtida);

        Optional<Curtida> encontrada = curtidaRepository.findById(salva.getId());
        assertTrue(encontrada.isPresent());
        assertNotNull(encontrada.get().getPost());
    }

    @Test
    void testListarTodasCurtidas() {
        Curtida curtida1 = Curtida.builder().usuario(usuario).post(post).build();
        Curtida curtida2 = Curtida.builder().usuario(usuario).comentario(comentario).build();
        entityManager.persistAndFlush(curtida1);
        entityManager.persistAndFlush(curtida2);

        assertEquals(2, curtidaRepository.findAll().size());
    }

    @Test
    void testBuscarCurtidasPorPost() {
        Curtida curtida1 = Curtida.builder().usuario(usuario).post(post).build();
        Curtida curtida2 = Curtida.builder().usuario(usuario).post(post).build();
        entityManager.persistAndFlush(curtida1);
        entityManager.persistAndFlush(curtida2);

        List<Curtida> curtidas = curtidaRepository.findByPostId(post.getId());
        assertEquals(2, curtidas.size());
    }

    @Test
    void testBuscarCurtidasPorComentario() {
        Curtida curtida1 = Curtida.builder().usuario(usuario).comentario(comentario).build();
        Curtida curtida2 = Curtida.builder().usuario(usuario).comentario(comentario).build();
        entityManager.persistAndFlush(curtida1);
        entityManager.persistAndFlush(curtida2);

        List<Curtida> curtidas = curtidaRepository.findByComentarioId(comentario.getId());
        assertEquals(2, curtidas.size());
    }

    @Test
    void testBuscarCurtidasPorUsuario() {
        Curtida curtida1 = Curtida.builder().usuario(usuario).post(post).build();
        Curtida curtida2 = Curtida.builder().usuario(usuario).comentario(comentario).build();
        entityManager.persistAndFlush(curtida1);
        entityManager.persistAndFlush(curtida2);

        List<Curtida> curtidas = curtidaRepository.findByUsuarioId(usuario.getId());
        assertEquals(2, curtidas.size());
    }

    @Test
    void testDeletarCurtida() {
        Curtida curtida = Curtida.builder()
                .usuario(usuario)
                .post(post)
                .build();
        Curtida salva = entityManager.persistAndFlush(curtida);

        curtidaRepository.delete(salva);
        entityManager.flush();

        assertFalse(curtidaRepository.existsById(salva.getId()));
    }
}

