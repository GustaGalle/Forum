package com.ifsp.forum.controller;

import com.ifsp.forum.business.TopicoService;
import com.ifsp.forum.infrastructure.dto.TopicoRequestDTO;
import com.ifsp.forum.infrastructure.dto.TopicoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topico")
@RequiredArgsConstructor
public class TopicoController {

    private final TopicoService topicoService;

    @PostMapping("/salvar")
    public ResponseEntity<Void> salvarTopico(@RequestBody TopicoRequestDTO dto) {
        topicoService.salvarTopico(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<TopicoResponseDTO>> listarTopicos() {
        return ResponseEntity.ok(topicoService.listarTopicos());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<TopicoResponseDTO> buscarTopicoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(topicoService.buscarTopicoPorId(id));
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Void> atualizarTopico(@PathVariable Long id, @RequestBody TopicoRequestDTO dto) {
        topicoService.atualizarTopico(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarTopico(@PathVariable Long id) {
        topicoService.deletarTopicoPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/forum/{forumId}")
    public ResponseEntity<List<TopicoResponseDTO>> buscarTopicosPorForum(@PathVariable Long forumId) {
        return ResponseEntity.ok(topicoService.buscarTopicosPorForum(forumId));
    }
}
