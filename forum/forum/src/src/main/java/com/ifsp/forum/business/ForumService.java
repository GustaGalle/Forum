package com.ifsp.forum.business;

import com.ifsp.forum.infrastructure.dto.ForumRequestDTO;
import com.ifsp.forum.infrastructure.dto.ForumResponseDTO;
import com.ifsp.forum.infrastructure.entidys.Forum;
import com.ifsp.forum.infrastructure.entidys.TipoUsuario;
import com.ifsp.forum.infrastructure.entidys.Usuario;
import com.ifsp.forum.infrastructure.repository.ForumRepository;
import com.ifsp.forum.infrastructure.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ForumService {

    private final ForumRepository forumRepository;
    private final UsuarioRepository usuarioRepository;

    public ForumService(ForumRepository forumRepository, UsuarioRepository usuarioRepository) {
        this.forumRepository = forumRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void salvarForum(ForumRequestDTO forumRequestDTO) {
        // Validação: apenas PROFESSOR ou ALUNO_VETERANO podem criar fóruns
        if (forumRequestDTO.getUsuarioId() == null) {
            throw new RuntimeException("ID do usuário é obrigatório para criar um fórum");
        }

        Usuario usuario = usuarioRepository.findById(forumRequestDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuario.getTipo() != TipoUsuario.PROFESSOR && usuario.getTipo() != TipoUsuario.ALUNO_VETERANO) {
            throw new RuntimeException("Apenas professores ou alunos veteranos podem criar fóruns");
        }

        Forum forum = Forum.builder()
                .nome(forumRequestDTO.getNome())
                .descricao(forumRequestDTO.getDescricao())
                .build();

        forumRepository.saveAndFlush(forum);
    }

    public List<ForumResponseDTO> listarForuns() {
        return forumRepository.findAll().stream().map(forum -> ForumResponseDTO.builder()
                        .id(forum.getId())
                        .nome(forum.getNome())
                        .descricao(forum.getDescricao())
                        .usuariosIds(forum.getUsuarios() != null
                                ? forum.getUsuarios().stream().map(u -> u.getId()).collect(Collectors.toList())
                                : null)
                        .topicosIds(forum.getTopicos() != null
                                ? forum.getTopicos().stream().map(t -> t.getId()).collect(Collectors.toList())
                                : null)
                        .build())
                .collect(Collectors.toList());
    }

    public ForumResponseDTO buscarForumPorId(Long id) {
        Forum forum = forumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fórum não encontrado"));

        return ForumResponseDTO.builder()
                .id(forum.getId())
                .nome(forum.getNome())
                .descricao(forum.getDescricao())
                .usuariosIds(forum.getUsuarios() != null
                        ? forum.getUsuarios().stream().map(u -> u.getId()).collect(Collectors.toList())
                        : null)
                .topicosIds(forum.getTopicos() != null
                        ? forum.getTopicos().stream().map(t -> t.getId()).collect(Collectors.toList())
                        : null)
                .build();
    }

    public void atualizarForum(Long id, ForumRequestDTO forumRequestDTO) {
        Forum forumExistente = forumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fórum não encontrado"));

        if (forumRequestDTO.getNome() != null) {
            forumExistente.setNome(forumRequestDTO.getNome());
        }
        if (forumRequestDTO.getDescricao() != null) {
            forumExistente.setDescricao(forumRequestDTO.getDescricao());
        }

        forumRepository.saveAndFlush(forumExistente);
    }

    public void deletarForum(Long id) {
        Forum forum = forumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fórum não encontrado"));
        forumRepository.delete(forum);
    }
}
