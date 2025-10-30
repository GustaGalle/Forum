package com.ifsp.forum.business;

import com.ifsp.forum.infrastructure.entidys.Topico;
import com.ifsp.forum.infrastructure.entidys.Forum;
import com.ifsp.forum.infrastructure.repository.TopicoRepository;
import com.ifsp.forum.infrastructure.repository.ForumRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicoService {

    private final TopicoRepository topicoRepository;
    private final ForumRepository forumRepository;

    public TopicoService(TopicoRepository topicoRepository, ForumRepository forumRepository) {
        this.topicoRepository = topicoRepository;
        this.forumRepository = forumRepository;
    }

    // Salvar tópico
    public void salvarTopico(Topico topico) {
        if (topico.getForum() == null || topico.getForum().getId() == null) {
            throw new RuntimeException("O fórum precisa ser informado");
        }

        Forum forum = forumRepository.findById(topico.getForum().getId())
                .orElseThrow(() -> new RuntimeException("Fórum não encontrado"));

        topico.setForum(forum);
        topicoRepository.saveAndFlush(topico);
    }

    // Buscar tópico por ID
    public Topico buscarTopicoPorId(Long id) {
        return topicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tópico não encontrado"));
    }

    // Listar todos os tópicos
    public List<Topico> listarTopicos() {
        return topicoRepository.findAll();
    }

    // Atualizar tópico
    public void atualizarTopico(Long id, Topico topico) {
        Topico topicoExistente = buscarTopicoPorId(id);
        topicoExistente.setTitulo(topico.getTitulo() != null ? topico.getTitulo() : topicoExistente.getTitulo());
        topicoExistente.setDescricao(topico.getDescricao() != null ? topico.getDescricao() : topicoExistente.getDescricao());
        topicoRepository.saveAndFlush(topicoExistente);
    }

    // Deletar tópico por ID
    public void deletarTopicoPorId(Long id) {
        Optional<Topico> topico = topicoRepository.findById(id);
        if (topico.isPresent()) {
            topicoRepository.delete(topico.get());
        } else {
            throw new RuntimeException("Tópico com ID " + id + " não encontrado.");
        }
    }

    // Buscar tópicos por fórum
    public List<Topico> buscarTopicosPorForum(Long forumId) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new RuntimeException("Fórum não encontrado"));
        return topicoRepository.findByForumId(forum.getId());
    }
}
