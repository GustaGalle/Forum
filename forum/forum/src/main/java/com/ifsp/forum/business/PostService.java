package com.ifsp.forum.business;

import com.ifsp.forum.infrastructure.entidys.Post;
import com.ifsp.forum.infrastructure.entidys.Topico;
import com.ifsp.forum.infrastructure.entidys.Usuario;
import com.ifsp.forum.infrastructure.repository.PostRepository;
import com.ifsp.forum.infrastructure.repository.TopicoRepository;
import com.ifsp.forum.infrastructure.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final TopicoRepository topicoRepository;
    private final UsuarioRepository usuarioRepository;

    public PostService(PostRepository postRepository, TopicoRepository topicoRepository, UsuarioRepository usuarioRepository) {
        this.postRepository = postRepository;
        this.topicoRepository = topicoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void salvarPost(Post post, Long topicoId, Long usuarioId) {
        Topico topico = topicoRepository.findById(topicoId)
                .orElseThrow(() -> new RuntimeException("Tópico não encontrado"));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        post.setTopico(topico);
        post.setUsuario(usuario);

        postRepository.saveAndFlush(post);
    }

    // Buscar post por ID
    public Post buscarPostPorId(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));
    }

    // Listar todos os posts
    public List<Post> listarPosts() {
        return postRepository.findAll();
    }

    // Atualizar post
    public void atualizarPost(Long id, Post post) {
        Post postExistente = buscarPostPorId(id);
        postExistente.setDescricao(post.getDescricao() != null ? post.getDescricao() : postExistente.getDescricao());
        postExistente.setConteudo(post.getConteudo() != null ? post.getConteudo() : postExistente.getConteudo());
        postRepository.saveAndFlush(postExistente);
    }

    // Deletar post por ID
    public void deletarPostPorId(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            postRepository.delete(post.get());
        } else {
            throw new RuntimeException("Post com ID " + id + " não encontrado.");
        }
    }

    // Buscar posts por tópico
    public List<Post> buscarPostsPorTopico(Long topicoId) {
        return postRepository.findByTopicoId(topicoId);
    }

    // Buscar posts por usuário
    public List<Post> buscarPostsPorUsuario(Long usuarioId) {
        return postRepository.findByUsuarioId(usuarioId);
    }
}
