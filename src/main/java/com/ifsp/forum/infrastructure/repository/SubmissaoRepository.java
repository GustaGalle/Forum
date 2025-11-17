package com.ifsp.forum.infrastructure.repository;

import com.ifsp.forum.infrastructure.entidys.Submissao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissaoRepository extends JpaRepository<Submissao, Long> {
    List<Submissao> findByUsuarioId(Long usuarioId);
    List<Submissao> findByAlgoritmoId(Long algoritmoId);
}
