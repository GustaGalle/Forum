package com.ifsp.forum.infrastructure.repository;

import com.ifsp.forum.infrastructure.entidys.Curtida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurtidaRepository extends JpaRepository<Curtida, Long> {
    List<Curtida> findByPostId(Long postId);
    List<Curtida> findByComentarioId(Long comentarioId);
    List<Curtida> findByUsuarioId(Long usuarioId);
}
