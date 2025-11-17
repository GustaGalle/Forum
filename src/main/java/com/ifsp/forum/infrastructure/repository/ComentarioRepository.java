package com.ifsp.forum.infrastructure.repository;

import com.ifsp.forum.infrastructure.entidys.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByPostId(Long postId);
    List<Comentario> findByUsuarioId(Long usuarioId);
}
