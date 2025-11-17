package com.ifsp.forum.controller;

import com.ifsp.forum.business.Judge0Service;
import com.ifsp.forum.business.SubmissaoService;
import com.ifsp.forum.infrastructure.dto.SubmissaoDTO;
import com.ifsp.forum.infrastructure.dto.SubmissaoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/submissao")
@RequiredArgsConstructor
public class SubmissaoController {

    private final SubmissaoService submissaoService;
    private final Judge0Service judge0Service; // injete o Judge0Service

    @PostMapping("/criar")
    public ResponseEntity<SubmissaoResponseDTO> criar(@RequestBody SubmissaoDTO dto) {
        return ResponseEntity.ok(submissaoService.criarSubmissao(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubmissaoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(submissaoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<SubmissaoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(submissaoService.listarSubmissoes());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<SubmissaoResponseDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(submissaoService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/algoritmo/{algoritmoId}")
    public ResponseEntity<List<SubmissaoResponseDTO>> listarPorAlgoritmo(@PathVariable Long algoritmoId) {
        return ResponseEntity.ok(submissaoService.listarPorAlgoritmo(algoritmoId));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        submissaoService.deletarSubmissao(id);
        return ResponseEntity.noContent().build();
    }

    // 🔹 Aqui vai o endpoint de avaliação
    @PostMapping("/avaliar/{id}")
    public ResponseEntity<SubmissaoResponseDTO> avaliar(@PathVariable Long id) {
        return ResponseEntity.ok(judge0Service.avaliarSubmissao(id));
    }
}