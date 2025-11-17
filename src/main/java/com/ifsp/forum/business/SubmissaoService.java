package com.ifsp.forum.business;

import com.ifsp.forum.infrastructure.dto.SubmissaoDTO;
import com.ifsp.forum.infrastructure.dto.SubmissaoResponseDTO;
import com.ifsp.forum.infrastructure.entidys.Algoritmo;
import com.ifsp.forum.infrastructure.entidys.Submissao;
import com.ifsp.forum.infrastructure.entidys.Usuario;
import com.ifsp.forum.infrastructure.repository.AlgoritmoRepository;
import com.ifsp.forum.infrastructure.repository.SubmissaoRepository;
import com.ifsp.forum.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmissaoService {

    private final SubmissaoRepository submissaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AlgoritmoRepository algoritmoRepository;

    // Criar submissão
    public SubmissaoResponseDTO criarSubmissao(SubmissaoDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Algoritmo algoritmo = algoritmoRepository.findById(dto.getAlgoritmoId())
                .orElseThrow(() -> new RuntimeException("Algoritmo não encontrado"));

        // Por enquanto, apenas salvamos o código; avaliação com Judge0 será futura
        Submissao submissao = Submissao.builder()
                .usuario(usuario)
                .algoritmo(algoritmo)
                .codigo(dto.getCodigo())
                .aprovado(null)
                .feedback(null)
                .build();

        Submissao salvo = submissaoRepository.save(submissao);
        return mapToDTO(salvo);
    }

    // Listar todas submissões
    public List<SubmissaoResponseDTO> listarSubmissoes() {
        return submissaoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Buscar submissão por ID
    public SubmissaoResponseDTO buscarPorId(Long id) {
        return submissaoRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Submissão não encontrada"));
    }

    // Listar submissões por usuário
    public List<SubmissaoResponseDTO> listarPorUsuario(Long usuarioId) {
        return submissaoRepository.findByUsuarioId(usuarioId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Listar submissões por algoritmo
    public List<SubmissaoResponseDTO> listarPorAlgoritmo(Long algoritmoId) {
        return submissaoRepository.findByAlgoritmoId(algoritmoId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Deletar submissão
    public void deletarSubmissao(Long id) {
        if (!submissaoRepository.existsById(id))
            throw new RuntimeException("Submissão não encontrada");
        submissaoRepository.deleteById(id);
    }

    private SubmissaoResponseDTO mapToDTO(Submissao submissao) {
        return SubmissaoResponseDTO.builder()
                .id(submissao.getId())
                .usuarioId(submissao.getUsuario().getId())
                .algoritmoId(submissao.getAlgoritmo().getId())
                .codigo(submissao.getCodigo())
                .aprovado(submissao.getAprovado())
                .feedback(submissao.getFeedback())
                .build();
    }
}
