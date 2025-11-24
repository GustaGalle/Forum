package com.ifsp.forum.business;

import com.ifsp.forum.infrastructure.dto.PostRequestDTO;
import com.ifsp.forum.infrastructure.dto.PostResponseDTO;
import com.ifsp.forum.infrastructure.entidys.Post;
import com.ifsp.forum.infrastructure.entidys.Topico;
import com.ifsp.forum.infrastructure.entidys.Usuario;
import com.ifsp.forum.infrastructure.repository.PostRepository;
import com.ifsp.forum.infrastructure.repository.TopicoRepository;
import com.ifsp.forum.infrastructure.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public void salvarPost(PostRequestDTO dto) {
        Topico topico = topicoRepository.findById(dto.getTopicoId())
                .orElseThrow(() -> new RuntimeException("Tópico não encontrado"));
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Post post = Post.builder()
                .conteudo(dto.getConteudo())
                .descricao(dto.getDescricao())
                .topico(topico)
                .usuario(usuario)
                .build();

        postRepository.saveAndFlush(post);
    }

    public PostResponseDTO buscarPostPorId(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));
        return toResponse(post);
    }

    public List<PostResponseDTO> listarPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void atualizarPost(Long id, PostRequestDTO dto) {
        Post postExistente = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        if (dto.getDescricao() != null) postExistente.setDescricao(dto.getDescricao());
        if (dto.getConteudo() != null) postExistente.setConteudo(dto.getConteudo());

        postRepository.saveAndFlush(postExistente);
    }

    public void deletarPostPorId(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post com ID " + id + " não encontrado."));
        postRepository.delete(post);
    }

    public List<PostResponseDTO> buscarPostsPorTopico(Long topicoId) {
        return postRepository.findByTopicoId(topicoId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<PostResponseDTO> buscarPostsPorUsuario(Long usuarioId) {
        return postRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private PostResponseDTO toResponse(Post post) {
        return PostResponseDTO.builder()
                .id(post.getId())
                .conteudo(post.getConteudo())
                .descricao(post.getDescricao())
                .topicoId(post.getTopico() != null ? post.getTopico().getId() : null)
                .usuarioId(post.getUsuario() != null ? post.getUsuario().getId() : null)
                .build();
    }
}
