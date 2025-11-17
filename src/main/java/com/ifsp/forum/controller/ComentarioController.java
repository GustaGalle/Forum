package com.ifsp.forum.controller;

import com.ifsp.forum.business.ComentarioService;
import com.ifsp.forum.infrastructure.dto.ComentarioRequestDTO;
import com.ifsp.forum.infrastructure.dto.ComentarioResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comentario")
@RequiredArgsConstructor
public class ComentarioController {

    private final ComentarioService comentarioService;

    // Salvar comentário
    @PostMapping("/salvar")
    public ResponseEntity<ComentarioResponseDTO> salvarComentario(@RequestBody ComentarioRequestDTO dto) {
        return ResponseEntity.ok(comentarioService.salvarComentario(dto));
    }

    // Listar todos os comentários
    @GetMapping
    public ResponseEntity<List<ComentarioResponseDTO>> listarComentarios() {
        return ResponseEntity.ok(comentarioService.listarComentarios());
    }

    // Buscar comentário por ID
    @GetMapping("/{id}")
    public ResponseEntity<ComentarioResponseDTO> buscarComentarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(comentarioService.buscarComentarioPorId(id));
    }

    // Atualizar comentário
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<ComentarioResponseDTO> atualizarComentario(@PathVariable Long id,
                                                                     @RequestBody ComentarioRequestDTO dto) {
        return ResponseEntity.ok(comentarioService.atualizarComentario(id, dto));
    }

    // Deletar comentário
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarComentario(@PathVariable Long id) {
        comentarioService.deletarComentarioPorId(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar comentários por post
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<ComentarioResponseDTO>> buscarComentariosPorPost(@PathVariable Long postId) {
        return ResponseEntity.ok(comentarioService.buscarComentariosPorPost(postId));
    }

    // Buscar comentários por usuário
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ComentarioResponseDTO>> buscarComentariosPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(comentarioService.buscarComentariosPorUsuario(usuarioId));
    }
}
