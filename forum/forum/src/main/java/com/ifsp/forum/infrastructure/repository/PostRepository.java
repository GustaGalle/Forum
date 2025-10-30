package com.ifsp.forum.infrastructure.repository;

import com.ifsp.forum.infrastructure.entidys.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTopicoId(Long topicoId);
    List<Post> findByUsuarioId(Long usuarioId);
}
