package com.ifsp.forum.business;

import com.ifsp.forum.infrastructure.dto.ComentarioRequestDTO;
import com.ifsp.forum.infrastructure.dto.ComentarioResponseDTO;
import com.ifsp.forum.infrastructure.entidys.Comentario;
import com.ifsp.forum.infrastructure.entidys.Post;
import com.ifsp.forum.infrastructure.entidys.Usuario;
import com.ifsp.forum.infrastructure.repository.ComentarioRepository;
import com.ifsp.forum.infrastructure.repository.PostRepository;
import com.ifsp.forum.infrastructure.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public ComentarioResponseDTO salvarComentario(ComentarioRequestDTO dto) {
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Comentario comentario = Comentario.builder()
                .mensagem(dto.getMensagem())
                .post(post)
                .usuario(usuario)
                .build();

        Comentario salvo = comentarioRepository.saveAndFlush(comentario);

        return mapToResponse(salvo);
    }

    // Buscar por ID
    public ComentarioResponseDTO buscarComentarioPorId(Long id) {
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentário não encontrado"));
        return mapToResponse(comentario);
    }

    // Listar todos
    public List<ComentarioResponseDTO> listarComentarios() {
        return comentarioRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Atualizar
    public ComentarioResponseDTO atualizarComentario(Long id, ComentarioRequestDTO dto) {
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentário não encontrado"));

        comentario.setMensagem(dto.getMensagem() != null ? dto.getMensagem() : comentario.getMensagem());
        Comentario atualizado = comentarioRepository.saveAndFlush(comentario);

        return mapToResponse(atualizado);
    }

    // Deletar
    public void deletarComentarioPorId(Long id) {
        if (!comentarioRepository.existsById(id)) {
            throw new RuntimeException("Comentário com ID " + id + " não encontrado.");
        }
        comentarioRepository.deleteById(id);
    }

    // Buscar por post
    public List<ComentarioResponseDTO> buscarComentariosPorPost(Long postId) {
        return comentarioRepository.findByPostId(postId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Buscar por usuário
    public List<ComentarioResponseDTO> buscarComentariosPorUsuario(Long usuarioId) {
        return comentarioRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Conversão manual (sem mapper)
    private ComentarioResponseDTO mapToResponse(Comentario comentario) {
        ComentarioResponseDTO dto = new ComentarioResponseDTO();
        dto.setId(comentario.getId());
        dto.setMensagem(comentario.getMensagem());
        dto.setPostId(comentario.getPost() != null ? comentario.getPost().getId() : null);
        dto.setNomeUsuario(comentario.getUsuario() != null ? comentario.getUsuario().getNome() : null);
        return dto;
    }
}
