package com.ifsp.forum.infrastructure.repository;

import com.ifsp.forum.infrastructure.entidys.Topico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    List<Topico> findByForumId(Long forumId);
}
