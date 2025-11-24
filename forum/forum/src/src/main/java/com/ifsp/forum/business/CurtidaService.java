package com.ifsp.forum.business;

import com.ifsp.forum.infrastructure.dto.CurtidaRequestDTO;
import com.ifsp.forum.infrastructure.dto.CurtidaResponseDTO;
import com.ifsp.forum.infrastructure.entidys.Comentario;
import com.ifsp.forum.infrastructure.entidys.Curtida;
import com.ifsp.forum.infrastructure.entidys.Post;
import com.ifsp.forum.infrastructure.entidys.Usuario;
import com.ifsp.forum.infrastructure.repository.ComentarioRepository;
import com.ifsp.forum.infrastructure.repository.CurtidaRepository;
import com.ifsp.forum.infrastructure.repository.PostRepository;
import com.ifsp.forum.infrastructure.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public CurtidaResponseDTO salvarCurtida(CurtidaRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Curtida curtida = Curtida.builder().usuario(usuario).build();

        if (dto.getPostId() != null) {
            Post post = postRepository.findById(dto.getPostId())
                    .orElseThrow(() -> new RuntimeException("Post não encontrado"));
            curtida.setPost(post);
        }

        if (dto.getComentarioId() != null) {
            Comentario comentario = comentarioRepository.findById(dto.getComentarioId())
                    .orElseThrow(() -> new RuntimeException("Comentário não encontrado"));
            curtida.setComentario(comentario);
        }

        Curtida salva = curtidaRepository.saveAndFlush(curtida);
        return mapToResponse(salva);
    }

    // Buscar por ID
    public CurtidaResponseDTO buscarCurtidaPorId(Long id) {
        Curtida curtida = curtidaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curtida não encontrada"));
        return mapToResponse(curtida);
    }

    // Listar todas
    public List<CurtidaResponseDTO> listarCurtidas() {
        return curtidaRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Deletar
    public void deletarCurtidaPorId(Long id) {
        if (!curtidaRepository.existsById(id)) {
            throw new RuntimeException("Curtida com ID " + id + " não encontrada.");
        }
        curtidaRepository.deleteById(id);
    }

    // Buscar curtidas por post
    public List<CurtidaResponseDTO> buscarCurtidasPorPost(Long postId) {
        return curtidaRepository.findByPostId(postId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Buscar curtidas por comentário
    public List<CurtidaResponseDTO> buscarCurtidasPorComentario(Long comentarioId) {
        return curtidaRepository.findByComentarioId(comentarioId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Buscar curtidas por usuário
    public List<CurtidaResponseDTO> buscarCurtidasPorUsuario(Long usuarioId) {
        return curtidaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Conversão manual (entidade -> DTO)
    private CurtidaResponseDTO mapToResponse(Curtida curtida) {
        CurtidaResponseDTO dto = new CurtidaResponseDTO();
        dto.setId(curtida.getId());
        dto.setNomeUsuario(curtida.getUsuario() != null ? curtida.getUsuario().getNome() : null);
        dto.setPostId(curtida.getPost() != null ? curtida.getPost().getId() : null);
        dto.setComentarioId(curtida.getComentario() != null ? curtida.getComentario().getId() : null);
        return dto;
    }
}
