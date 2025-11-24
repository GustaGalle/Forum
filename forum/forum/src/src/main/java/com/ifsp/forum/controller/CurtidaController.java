package com.ifsp.forum.controller;

import com.ifsp.forum.business.CurtidaService;
import com.ifsp.forum.infrastructure.dto.CurtidaRequestDTO;
import com.ifsp.forum.infrastructure.dto.CurtidaResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/curtida")
@RequiredArgsConstructor
public class CurtidaController {

    private final CurtidaService curtidaService;

    // Salvar curtida
    @PostMapping("/salvar")
    public ResponseEntity<CurtidaResponseDTO> salvarCurtida(@RequestBody CurtidaRequestDTO dto) {
        return ResponseEntity.ok(curtidaService.salvarCurtida(dto));
    }

    // Listar todas as curtidas
    @GetMapping
    public ResponseEntity<List<CurtidaResponseDTO>> listarCurtidas() {
        return ResponseEntity.ok(curtidaService.listarCurtidas());
    }

    // Buscar curtida por ID
    @GetMapping("/{id}")
    public ResponseEntity<CurtidaResponseDTO> buscarCurtidaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(curtidaService.buscarCurtidaPorId(id));
    }

    // Deletar curtida
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarCurtida(@PathVariable Long id) {
        curtidaService.deletarCurtidaPorId(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar curtidas por post
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CurtidaResponseDTO>> buscarCurtidasPorPost(@PathVariable Long postId) {
        return ResponseEntity.ok(curtidaService.buscarCurtidasPorPost(postId));
    }

    // Buscar curtidas por comentário
    @GetMapping("/comentario/{comentarioId}")
    public ResponseEntity<List<CurtidaResponseDTO>> buscarCurtidasPorComentario(@PathVariable Long comentarioId) {
        return ResponseEntity.ok(curtidaService.buscarCurtidasPorComentario(comentarioId));
    }

    // Buscar curtidas por usuário
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CurtidaResponseDTO>> buscarCurtidasPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(curtidaService.buscarCurtidasPorUsuario(usuarioId));
    }
}
