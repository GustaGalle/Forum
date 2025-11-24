package com.ifsp.forum.business;

import com.ifsp.forum.infrastructure.dto.AlgoritmoRequestDTO;
import com.ifsp.forum.infrastructure.dto.AlgoritmoResponseDTO;
import com.ifsp.forum.infrastructure.entidys.Algoritmo;
import com.ifsp.forum.infrastructure.repository.AlgoritmoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlgoritmoService {

    private final AlgoritmoRepository algoritmoRepository;

    // Criar novo algoritmo
    public AlgoritmoResponseDTO criarAlgoritmo(AlgoritmoRequestDTO dto) {
        Algoritmo algoritmo = Algoritmo.builder()
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .linguagemPermitida(dto.getLinguagemPermitida())
                .gabarito(dto.getGabarito())
                .judge0LanguageId(dto.getJudge0LanguageId())
                .casosDeTeste(dto.getCasosDeTeste()) // ✅ agora recebe os casos de teste
                .build();

        Algoritmo salvo = algoritmoRepository.save(algoritmo);
        return mapToDTO(salvo);
    }

    public AlgoritmoResponseDTO atualizarAlgoritmo(Long id, AlgoritmoRequestDTO dto) {
        Algoritmo algoritmo = algoritmoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Algoritmo não encontrado"));

        algoritmo.setTitulo(dto.getTitulo() != null ? dto.getTitulo() : algoritmo.getTitulo());
        algoritmo.setDescricao(dto.getDescricao() != null ? dto.getDescricao() : algoritmo.getDescricao());
        algoritmo.setLinguagemPermitida(dto.getLinguagemPermitida() != null ? dto.getLinguagemPermitida() : algoritmo.getLinguagemPermitida());
        algoritmo.setGabarito(dto.getGabarito() != null ? dto.getGabarito() : algoritmo.getGabarito());
        algoritmo.setJudge0LanguageId(dto.getJudge0LanguageId() != null ? dto.getJudge0LanguageId() : algoritmo.getJudge0LanguageId());
        algoritmo.setCasosDeTeste(dto.getCasosDeTeste() != null ? dto.getCasosDeTeste() : algoritmo.getCasosDeTeste());

        Algoritmo atualizado = algoritmoRepository.save(algoritmo);
        return mapToDTO(atualizado);
    }

    // Buscar algoritmo por id
    public AlgoritmoResponseDTO buscarPorId(Long id) {
        return algoritmoRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Algoritmo não encontrado"));
    }

    // Listar todos os algoritmos
    public List<AlgoritmoResponseDTO> listarTodos() {
        return algoritmoRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Deletar algoritmo
    public void deletarAlgoritmo(Long id) {
        if (!algoritmoRepository.existsById(id))
            throw new RuntimeException("Algoritmo não encontrado");

        algoritmoRepository.deleteById(id);
    }

    // Converter entidade → DTO de resposta
    private AlgoritmoResponseDTO mapToDTO(Algoritmo algoritmo) {
        return AlgoritmoResponseDTO.builder()
                .id(algoritmo.getId())
                .titulo(algoritmo.getTitulo())
                .descricao(algoritmo.getDescricao())
                .linguagemPermitida(algoritmo.getLinguagemPermitida())
                .gabarito(algoritmo.getGabarito())
                .judge0LanguageId(algoritmo.getJudge0LanguageId())
                .casosDeTeste(algoritmo.getCasosDeTeste())
                .build();
    }
}
