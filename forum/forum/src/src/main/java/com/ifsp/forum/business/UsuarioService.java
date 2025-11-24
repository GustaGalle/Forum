package com.ifsp.forum.business;

import com.ifsp.forum.infrastructure.dto.UsuarioRequest;
import com.ifsp.forum.infrastructure.dto.UsuarioResponse;
import com.ifsp.forum.infrastructure.entidys.TipoUsuario;
import com.ifsp.forum.infrastructure.entidys.Usuario;
import com.ifsp.forum.infrastructure.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private UsuarioRepository usuarioRepository;
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void salvarUsuario(UsuarioRequest usuarioRequest) {
        // Validação: apenas PROFESSOR ou ALUNO_VETERANO podem criar usuários
        if (usuarioRequest.getUsuarioCriadorId() != null) {
            Usuario usuarioCriador = usuarioRepository.findById(usuarioRequest.getUsuarioCriadorId())
                    .orElseThrow(() -> new RuntimeException("Usuário criador não encontrado"));

            if (usuarioCriador.getTipo() != TipoUsuario.PROFESSOR && usuarioCriador.getTipo() != TipoUsuario.ALUNO_VETERANO) {
                throw new RuntimeException("Apenas professores ou alunos veteranos podem criar usuários");
            }
        }

        Usuario usuario = Usuario.builder()
                .nome(usuarioRequest.getNome())
                .email(usuarioRequest.getEmail())
                .password(usuarioRequest.getPassword())
                .tipo(usuarioRequest.getTipo() != null ? usuarioRequest.getTipo() : TipoUsuario.ALUNO)
                .build();
        usuarioRepository.saveAndFlush(usuario);
    }

    public UsuarioResponse buscarUsuarioPorNome(String nome) {
        Usuario usuario = usuarioRepository.findByNome(nome)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return mapToResponse(usuario);
    }

    public List<UsuarioResponse> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void deletarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário com ID " + id + " não encontrado."));
        usuarioRepository.delete(usuario);
    }

    public void atualizarUsuarioPorId(Long id, UsuarioRequest usuarioRequest) {
        Usuario usuarioEntity = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuarioEntity.setNome(usuarioRequest.getNome() != null ? usuarioRequest.getNome() : usuarioEntity.getNome());
        usuarioEntity.setEmail(usuarioRequest.getEmail() != null ? usuarioRequest.getEmail() : usuarioEntity.getEmail());
        usuarioEntity.setPassword(usuarioRequest.getPassword() != null ? usuarioRequest.getPassword() : usuarioEntity.getPassword());
        usuarioEntity.setTipo(usuarioRequest.getTipo() != null ? usuarioRequest.getTipo() : usuarioEntity.getTipo());

        usuarioRepository.saveAndFlush(usuarioEntity);
    }

    private UsuarioResponse mapToResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .build();
    }

}
