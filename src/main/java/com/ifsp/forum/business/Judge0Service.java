package com.ifsp.forum.business;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

        boolean aprovado = true;
        StringBuilder feedback = new StringBuilder();

        try {
            RestTemplate restTemplate = new RestTemplate();

            // Ler casos de teste do algoritmo
            List<Map<String, String>> casos = objectMapper.readValue(
                    algoritmo.getCasosDeTeste(),
                    new TypeReference<List<Map<String, String>>>() {}
            );

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
                headers.set("X-RapidAPI-Key", "a0901aebcbmsh9f8745681497560p1c9e05jsn6a7d905366c");
                headers.set("X-RapidAPI-Host", "judge0-ce.p.rapidapi.com");

                String jsonBody = objectMapper.writeValueAsString(body);
                HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
                String url = "https://judge0-ce.p.rapidapi.com/submissions?base64_encoded=false&wait=true";

                ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, request, Map.class);
                Map<String, Object> response = responseEntity.getBody();

                if (response == null) {
                    throw new RuntimeException("Resposta do Judge0 é nula");
                }

                // Pega saída
                String saida = response.get("stdout") != null ? response.get("stdout").toString() : "";
                String compileOutput = response.get("compile_output") != null ? response.get("compile_output").toString() : "";
                String stderr = response.get("stderr") != null ? response.get("stderr").toString() : "";

                boolean casoAprovado = saida.trim().equals(saidaEsperada.trim());
                if (!casoAprovado) {
                    aprovado = false;
                }

                feedback.append("Entrada: ").append(entrada)
                        .append("\nSaída esperada: ").append(saidaEsperada)
                        .append("\nSaída do código: ").append(saida.isEmpty() ? (compileOutput.isEmpty() ? stderr : compileOutput) : saida)
                        .append("\nStatus: ").append(casoAprovado ? "Aprovado" : "Falhou")
                        .append("\n\n");
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
            throw new RuntimeException("Erro ao avaliar submissão: " + e.getMessage(), e);
        }
    }
}
