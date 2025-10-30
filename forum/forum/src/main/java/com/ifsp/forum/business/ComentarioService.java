package com.ifsp.forum.business;

import com.ifsp.forum.infrastructure.entidys.Comentario;
import com.ifsp.forum.infrastructure.entidys.Post;
import com.ifsp.forum.infrastructure.entidys.Usuario;
import com.ifsp.forum.infrastructure.repository.ComentarioRepository;
import com.ifsp.forum.infrastructure.repository.PostRepository;
import com.ifsp.forum.infrastructure.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final PostRepository postRepository;
    private final UsuarioRepository usuarioRepository;

    public ComentarioService(ComentarioRepository comentarioRepository,
                             PostRepository postRepository,
                             UsuarioRepository usuarioRepository) {
        this.comentarioRepository = comentarioRepository;
        this.postRepository = postRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Salvar comentário
    public void salvarComentario(Comentario comentario, Long postId, Long usuarioId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        comentario.setPost(post);
        comentario.setUsuario(usuario);
        comentarioRepository.saveAndFlush(comentario);
    }

    // Buscar comentário por ID
    public Comentario buscarComentarioPorId(Long id) {
        return comentarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentário não encontrado"));
    }

    // Listar todos os comentários
    public List<Comentario> listarComentarios() {
        return comentarioRepository.findAll();
    }

    // Atualizar comentário
    public void atualizarComentario(Long id, Comentario comentario) {
        Comentario comentarioExistente = buscarComentarioPorId(id);
        comentarioExistente.setMensagem(comentario.getMensagem() != null ?
                comentario.getMensagem() : comentarioExistente.getMensagem());
        comentarioRepository.saveAndFlush(comentarioExistente);
    }

    // Deletar comentário por ID
    public void deletarComentarioPorId(Long id) {
        Optional<Comentario> comentario = comentarioRepository.findById(id);
        if (comentario.isPresent()) {
            comentarioRepository.delete(comentario.get());
        } else {
            throw new RuntimeException("Comentário com ID " + id + " não encontrado.");
        }
    }

    // Buscar comentários por post
    public List<Comentario> buscarComentariosPorPost(Long postId) {
        return comentarioRepository.findByPostId(postId);
    }

    // Buscar comentários por usuário
    public List<Comentario> buscarComentariosPorUsuario(Long usuarioId) {
        return comentarioRepository.findByUsuarioId(usuarioId);
    }
}
