package com.ifsp.forum.business;

import com.ifsp.forum.infrastructure.entidys.Curtida;
import com.ifsp.forum.infrastructure.entidys.Post;
import com.ifsp.forum.infrastructure.entidys.Comentario;
import com.ifsp.forum.infrastructure.entidys.Usuario;
import com.ifsp.forum.infrastructure.repository.CurtidaRepository;
import com.ifsp.forum.infrastructure.repository.PostRepository;
import com.ifsp.forum.infrastructure.repository.ComentarioRepository;
import com.ifsp.forum.infrastructure.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurtidaService {

    private final CurtidaRepository curtidaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PostRepository postRepository;
    private final ComentarioRepository comentarioRepository;

    public CurtidaService(CurtidaRepository curtidaRepository,
                          UsuarioRepository usuarioRepository,
                          PostRepository postRepository,
                          ComentarioRepository comentarioRepository) {
        this.curtidaRepository = curtidaRepository;
        this.usuarioRepository = usuarioRepository;
        this.postRepository = postRepository;
        this.comentarioRepository = comentarioRepository;
    }

    // Salvar curtida
    public void salvarCurtida(Curtida curtida, Long usuarioId, Long postId, Long comentarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        curtida.setUsuario(usuario);

        if (postId != null) {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new RuntimeException("Post não encontrado"));
            curtida.setPost(post);
        }

        if (comentarioId != null) {
            Comentario comentario = comentarioRepository.findById(comentarioId)
                    .orElseThrow(() -> new RuntimeException("Comentário não encontrado"));
            curtida.setComentario(comentario);
        }

        curtidaRepository.saveAndFlush(curtida);
    }

    // Buscar curtida por ID
    public Curtida buscarCurtidaPorId(Long id) {
        return curtidaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curtida não encontrada"));
    }

    // Listar todas as curtidas
    public List<Curtida> listarCurtidas() {
        return curtidaRepository.findAll();
    }

    // Deletar curtida por ID
    public void deletarCurtidaPorId(Long id) {
        Optional<Curtida> curtida = curtidaRepository.findById(id);
        if (curtida.isPresent()) {
            curtidaRepository.delete(curtida.get());
        } else {
            throw new RuntimeException("Curtida com ID " + id + " não encontrada.");
        }
    }

    // Buscar curtidas por post
    public List<Curtida> buscarCurtidasPorPost(Long postId) {
        return curtidaRepository.findByPostId(postId);
    }

    // Buscar curtidas por comentário
    public List<Curtida> buscarCurtidasPorComentario(Long comentarioId) {
        return curtidaRepository.findByComentarioId(comentarioId);
    }

    // Buscar curtidas por usuário
    public List<Curtida> buscarCurtidasPorUsuario(Long usuarioId) {
        return curtidaRepository.findByUsuarioId(usuarioId);
    }
}
