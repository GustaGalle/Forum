package com.ifsp.forum.infrastructure.repository;

import com.ifsp.forum.infrastructure.entidys.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByNome(String nome);

    @Transactional
    void deleteByEmail(String email);
}
