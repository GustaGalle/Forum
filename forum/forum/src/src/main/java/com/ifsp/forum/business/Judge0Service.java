package com.ifsp.forum.business;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifsp.forum.infrastructure.config.Judge0Config;
import com.ifsp.forum.infrastructure.dto.SubmissaoResponseDTO;
import com.ifsp.forum.infrastructure.entidys.Submissao;
import com.ifsp.forum.infrastructure.entidys.Algoritmo;
import com.ifsp.forum.infrastructure.repository.SubmissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class Judge0Service {

    private final SubmissaoRepository submissaoRepository;
    private final Judge0Config judge0Config;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SubmissaoResponseDTO avaliarSubmissao(Long submissaoId) {
        // Busca submissão
        Submissao submissao = submissaoRepository.findById(submissaoId)
                .orElseThrow(() -> new RuntimeException("Submissão não encontrada"));

        String codigo = submissao.getCodigo();
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new RuntimeException("Código da submissão está vazio");
        }

        Algoritmo algoritmo = submissao.getAlgoritmo();
        Integer languageId = algoritmo.getJudge0LanguageId();
        if (languageId == null) {
            throw new RuntimeException("O algoritmo não possui um Judge0 language ID definido");
        }

        if (algoritmo.getCasosDeTeste() == null || algoritmo.getCasosDeTeste().trim().isEmpty()) {
            throw new RuntimeException("O algoritmo não possui casos de teste definidos");
        }

        boolean aprovado = true;
        StringBuilder feedback = new StringBuilder();

        try {
            // Ler casos de teste do algoritmo
            List<Map<String, String>> casos = objectMapper.readValue(
                    algoritmo.getCasosDeTeste(),
                    new TypeReference<List<Map<String, String>>>() {}
            );

            int numeroCaso = 1;
            for (Map<String, String> caso : casos) {
                String entrada = caso.get("entrada");
                String saidaEsperada = caso.get("saidaEsperada");

                // Corpo da requisição para o Judge0
                Map<String, Object> body = new HashMap<>();
                body.put("source_code", codigo);
                body.put("language_id", languageId);
                body.put("stdin", entrada);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("X-RapidAPI-Key", judge0Config.getApiKey());
                headers.set("X-RapidAPI-Host", judge0Config.getApiHost());

                String jsonBody = objectMapper.writeValueAsString(body);
                HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
                String url = judge0Config.getApiUrl();

                @SuppressWarnings("unchecked")
                ResponseEntity<Map<String, Object>> responseEntity = (ResponseEntity<Map<String, Object>>) (ResponseEntity<?>) restTemplate.postForEntity(url, request, Map.class);
                Map<String, Object> response = responseEntity.getBody();

                if (response == null) {
                    throw new RuntimeException("Resposta do Judge0 é nula");
                }

                // Verifica status da execução
                Object statusObj = response.get("status");
                Integer statusId = statusObj != null ? Integer.parseInt(statusObj.toString()) : null;
                
                // Status 3 = Accepted (sucesso)
                // Status 4 = Wrong Answer
                // Status 5 = Time Limit Exceeded
                // Status 6 = Compilation Error
                // Status 7 = Runtime Error
                
                // Pega saída
                String saida = response.get("stdout") != null ? response.get("stdout").toString().trim() : "";
                String compileOutput = response.get("compile_output") != null ? response.get("compile_output").toString().trim() : "";
                String stderr = response.get("stderr") != null ? response.get("stderr").toString().trim() : "";
                
                boolean casoAprovado = false;
                
                // Se houver erro de compilação ou runtime, reprova
                if (statusId != null && (statusId == 6 || statusId == 7)) {
                    aprovado = false;
                    casoAprovado = false;
                    feedback.append("Caso de Teste ").append(numeroCaso)
                            .append(":\nEntrada: ").append(entrada)
                            .append("\nErro: ").append(compileOutput.isEmpty() ? stderr : compileOutput)
                            .append("\nStatus: Falhou (Erro de ").append(statusId == 6 ? "Compilação" : "Runtime").append(")")
                            .append("\n\n");
                } else {
                    // Compara saída esperada com saída obtida
                    casoAprovado = saida.equals(saidaEsperada.trim());
                    if (!casoAprovado) {
                        aprovado = false;
                    }
                    
                    feedback.append("Caso de Teste ").append(numeroCaso)
                            .append(":\nEntrada: ").append(entrada)
                            .append("\nSaída esperada: ").append(saidaEsperada)
                            .append("\nSaída do código: ").append(saida.isEmpty() ? "(vazia)" : saida)
                            .append("\nStatus: ").append(casoAprovado ? "Aprovado" : "Falhou")
                            .append("\n\n");
                }
                
                numeroCaso++;
            }

            // Atualiza submissão no banco
            submissao.setAprovado(aprovado);
            submissao.setFeedback(feedback.toString());
            submissaoRepository.save(submissao);

            // Retorna DTO atualizado
            return SubmissaoResponseDTO.builder()
                    .id(submissao.getId())
                    .usuarioId(submissao.getUsuario().getId())
                    .algoritmoId(submissao.getAlgoritmo().getId())
                    .codigo(submissao.getCodigo())
                    .aprovado(submissao.getAprovado())
                    .feedback(submissao.getFeedback())
                    .build();

        } catch (Exception e) {
            // Em caso de erro, marca como reprovado e salva o erro no feedback
            submissao.setAprovado(false);
            submissao.setFeedback("Erro ao avaliar submissão no Judge0: " + e.getMessage() + 
                                 "\nCausa: " + (e.getCause() != null ? e.getCause().getMessage() : "Desconhecida"));
            submissaoRepository.save(submissao);
            throw new RuntimeException("Erro ao avaliar submissão: " + e.getMessage(), e);
        }
    }
}
