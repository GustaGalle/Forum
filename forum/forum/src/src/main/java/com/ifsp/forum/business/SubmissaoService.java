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
    private final Judge0Service judge0Service;

    /**
     * Cria uma submissão e automaticamente a avalia no Judge0.
     * O campo 'aprovado' só é definido pelo Judge0 após a avaliação.
     */
    public SubmissaoResponseDTO criarSubmissao(SubmissaoDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Algoritmo algoritmo = algoritmoRepository.findById(dto.getAlgoritmoId())
                .orElseThrow(() -> new RuntimeException("Algoritmo não encontrado"));

        // Criar submissão inicialmente sem avaliação (aprovado = null)
        // O campo 'aprovado' só será definido pelo Judge0
        Submissao submissao = Submissao.builder()
                .usuario(usuario)
                .algoritmo(algoritmo)
                .codigo(dto.getCodigo())
                .aprovado(null) // Só será definido pelo Judge0
                .feedback(null)  // Só será definido pelo Judge0
                .build();

        Submissao salvo = submissaoRepository.save(submissao);
        
        // Avaliar automaticamente no Judge0
        // O Judge0Service irá definir o campo 'aprovado' baseado na avaliação real
        try {
            return judge0Service.avaliarSubmissao(salvo.getId());
        } catch (Exception e) {
            // Se houver erro na avaliação, retorna a submissão sem avaliação
            // mas registra o erro no feedback
            salvo.setFeedback("Erro ao avaliar submissão: " + e.getMessage());
            salvo.setAprovado(false);
            submissaoRepository.save(salvo);
            return mapToDTO(salvo);
        }
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
