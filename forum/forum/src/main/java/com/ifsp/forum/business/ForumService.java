package com.ifsp.forum.business;

import com.ifsp.forum.infrastructure.entidys.Forum;
import com.ifsp.forum.infrastructure.repository.ForumRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForumService {

    private final ForumRepository forumRepository;

    public ForumService(ForumRepository forumRepository) {
        this.forumRepository = forumRepository;
    }

    public void salvarForum(Forum forum) {
        forumRepository.saveAndFlush(forum);
    }

    public List<Forum> listarForuns() {
        return forumRepository.findAll();
    }

    public Forum buscarForumPorId(Long id) {
        return forumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fórum não encontrado"));
    }

    public void atualizarForum(Long id, Forum forum) {
        Forum forumExistente = buscarForumPorId(id);
        forumExistente.setNome(forum.getNome() != null ? forum.getNome() : forumExistente.getNome());
        forumExistente.setDescricao(forum.getDescricao() != null ? forum.getDescricao() : forumExistente.getDescricao());
        forumRepository.saveAndFlush(forumExistente);
    }

    public void deletarForum(Long id) {
        Forum forum = buscarForumPorId(id);
        forumRepository.delete(forum);
    }
}
