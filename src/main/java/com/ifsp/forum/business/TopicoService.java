package com.ifsp.forum.business;

import com.ifsp.forum.infrastructure.dto.TopicoRequestDTO;
import com.ifsp.forum.infrastructure.dto.TopicoResponseDTO;
import com.ifsp.forum.infrastructure.entidys.Forum;
import com.ifsp.forum.infrastructure.entidys.Topico;
import com.ifsp.forum.infrastructure.repository.ForumRepository;
import com.ifsp.forum.infrastructure.repository.TopicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicoService {

    private final TopicoRepository topicoRepository;
    private final ForumRepository forumRepository;

    public TopicoService(TopicoRepository topicoRepository, ForumRepository forumRepository) {
        this.topicoRepository = topicoRepository;
        this.forumRepository = forumRepository;
    }

    public void salvarTopico(TopicoRequestDTO dto) {
        Forum forum = forumRepository.findById(dto.getForumId())
                .orElseThrow(() -> new RuntimeException("Fórum não encontrado"));

        Topico topico = Topico.builder()
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .forum(forum)
                .build();

        topicoRepository.saveAndFlush(topico);
    }

    public List<TopicoResponseDTO> listarTopicos() {
        return topicoRepository.findAll().stream().map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    public TopicoResponseDTO buscarTopicoPorId(Long id) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tópico não encontrado"));
        return converterParaResponse(topico);
    }

    public void atualizarTopico(Long id, TopicoRequestDTO dto) {
        Topico topicoExistente = topicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tópico não encontrado"));

        if (dto.getTitulo() != null) topicoExistente.setTitulo(dto.getTitulo());
        if (dto.getDescricao() != null) topicoExistente.setDescricao(dto.getDescricao());
        if (dto.getForumId() != null) {
            Forum forum = forumRepository.findById(dto.getForumId())
                    .orElseThrow(() -> new RuntimeException("Fórum não encontrado"));
            topicoExistente.setForum(forum);
        }

        topicoRepository.saveAndFlush(topicoExistente);
    }

    public void deletarTopicoPorId(Long id) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tópico não encontrado"));
        topicoRepository.delete(topico);
    }

    public List<TopicoResponseDTO> buscarTopicosPorForum(Long forumId) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new RuntimeException("Fórum não encontrado"));

        return topicoRepository.findByForumId(forum.getId())
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    private TopicoResponseDTO converterParaResponse(Topico topico) {
        return TopicoResponseDTO.builder()
                .id(topico.getId())
                .titulo(topico.getTitulo())
                .descricao(topico.getDescricao())
                .forumId(topico.getForum() != null ? topico.getForum().getId() : null)
                .postsIds(topico.getPosts() != null
                        ? topico.getPosts().stream().map(p -> p.getId()).collect(Collectors.toList())
                        : null)
                .build();
    }
}
