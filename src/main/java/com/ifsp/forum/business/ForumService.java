package com.ifsp.forum.business;

import com.ifsp.forum.infrastructure.dto.ForumRequestDTO;
import com.ifsp.forum.infrastructure.dto.ForumResponseDTO;
import com.ifsp.forum.infrastructure.entidys.Forum;
import com.ifsp.forum.infrastructure.repository.ForumRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ForumService {

    private final ForumRepository forumRepository;

    public ForumService(ForumRepository forumRepository) {
        this.forumRepository = forumRepository;
    }

    public void salvarForum(ForumRequestDTO forumRequestDTO) {
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
